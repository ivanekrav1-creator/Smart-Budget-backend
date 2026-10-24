package ru.tbank.smart.budget.service;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.smart.budget.api.mapper.TransactionMapper;
import ru.tbank.smart.budget.dao.account.entity.Account;
import ru.tbank.smart.budget.dao.account.repository.AccountRepository;
import ru.tbank.smart.budget.dao.budget.repository.BudgetRepository;
import ru.tbank.smart.budget.dao.category.entity.Category;
import ru.tbank.smart.budget.dao.category.repository.CategoryRepository;
import ru.tbank.smart.budget.dao.transaction.entity.Transaction;
import ru.tbank.smart.budget.dao.transaction.repository.TransactionRepository;
import ru.tbank.smart.budget.dao.transaction.repository.TransactionSpecification;
import ru.tbank.smart.budget.event.TransactionSavedEvent;
import ru.tbank.smart.budget.exception.EntityNotFoundException;
import ru.tbank.smart.budget.kafka.consumer.dto.TransactionEvent;
import ru.tbank.smart.budget.model.OperationDto;
import ru.tbank.smart.budget.record.OperationRecord;

@Service
@Transactional(readOnly = true)
public class OperationService {

  private final TransactionRepository transactionRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final BudgetRepository budgetRepository;
  private final AccountRepository accountRepository;
  private final TransactionMapper transactionMapper;
  private final CategoryRepository categoryRepository;
  private static final Logger log = LoggerFactory.getLogger(OperationService.class);

  public OperationService(
      TransactionRepository transactionRepository,
      ApplicationEventPublisher eventPublisher,
      BudgetRepository budgetRepository,
      AccountRepository accountRepository,
      TransactionMapper transactionMapper,
      CategoryRepository categoryRepository) {
    this.transactionRepository = transactionRepository;
    this.eventPublisher = eventPublisher;
    this.budgetRepository = budgetRepository;
    this.accountRepository = accountRepository;
    this.transactionMapper = transactionMapper;
    this.categoryRepository = categoryRepository;
  }

  public List<OperationDto> getOperations(OperationRecord filter) {
    List<UUID> accountIds = accountRepository.findIdsByUserId(filter.userId());
    UUID filteredBudgetId = filter.budgetId();

    List<Long> categoryIds = Collections.emptyList();
    if (filteredBudgetId != null) {
      categoryIds =
          budgetRepository.findCategoryIdsByUserIdAndBudgetId(filter.userId(), filteredBudgetId);
      if (categoryIds.isEmpty()) {
        throw new EntityNotFoundException("Budget does not belong to user");
      }
    }

    Specification<Transaction> spec =
        Specification.where(TransactionSpecification.hasAccountIds(accountIds))
            .and(TransactionSpecification.hasFrom(filter.from()))
            .and(TransactionSpecification.hasTo(filter.to()))
            .and(TransactionSpecification.hasCategoryIds(categoryIds))
            .and(TransactionSpecification.withCategory());

    List<Transaction> transactions = transactionRepository.findAll(spec);

    return transactions.stream()
        .map(t -> transactionMapper.toOperationDto(t, filteredBudgetId))
        .toList();
  }

  @Transactional
  public void create(TransactionEvent event, Long categoryId) {
    Account account =
        accountRepository
            .findById(event.accountId())
            .orElseThrow(() -> new EntityNotFoundException("Account not found"));

    if (isBalanceInsufficient(account.getAmount(), event.trxnAmount())) {
      log.warn("Transaction skipped - insufficient balance for account: {}", event.accountId());
      return;
    }
    account.setAmount(account.getAmount().add(event.trxnAmount()));

    Category categoryProxy = categoryRepository.getReferenceById(categoryId);
    Transaction transaction = transactionMapper.toEntity(event, categoryProxy);
    transactionRepository.save(transaction);
    eventPublisher.publishEvent(new TransactionSavedEvent(transaction));
  }

  @Transactional
  public Transaction changeExclusionStatus(UUID operationId, boolean excluded) {
    Transaction transaction =
        transactionRepository
            .findById(operationId)
            .orElseThrow(
                () -> new EntityNotFoundException("Операция с id: " + operationId + " не найдена"));

    transaction.setExcluded(excluded);
    return transaction;
  }

  private boolean isBalanceInsufficient(BigDecimal balance, BigDecimal delta) {
    return balance.add(delta).compareTo(ZERO) < 0;
  }

  @Transactional
  public void updateCategory(UUID operationId, Long categoryId) {
    Transaction transaction =
        transactionRepository
            .findById(operationId)
            .orElseThrow(
                () -> new EntityNotFoundException("Операция с id: " + operationId + " не найдена"));
    if (!categoryRepository.existsById(categoryId)) {
      throw new EntityNotFoundException("Категория с id: " + categoryId + " не найдена");
    }
    Category category = categoryRepository.getReferenceById(categoryId);
    transaction.setCategory(category);
  }
}
