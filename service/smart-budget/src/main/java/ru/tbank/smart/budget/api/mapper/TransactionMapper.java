package ru.tbank.smart.budget.api.mapper;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Component;
import ru.tbank.smart.budget.dao.category.entity.Category;
import ru.tbank.smart.budget.dao.transaction.entity.Transaction;
import ru.tbank.smart.budget.kafka.consumer.dto.TransactionEvent;
import ru.tbank.smart.budget.model.AmountDto;
import ru.tbank.smart.budget.model.CategoryDto;
import ru.tbank.smart.budget.model.Currency;
import ru.tbank.smart.budget.model.OperationDto;
import ru.tbank.smart.budget.model.OperationType;

@Component
public class TransactionMapper {

  private OperationType resolveType(Transaction t) {
    return t.getAmountValue().compareTo(BigDecimal.ZERO) >= 0
        ? OperationType.CREDIT
        : OperationType.DEBIT;
  }

  public OperationDto toOperationDto(Transaction t, UUID budgetId) {
    AmountDto amount =
        new AmountDto(t.getAmountValue(), Currency.fromValue(t.getAmountCurrency().trim()));

    CategoryDto category = null;
    if (t.getCategory() != null) {
      Category c = t.getCategory();
      category = new CategoryDto(c.getId(), c.getName(), c.getIcon());
    }

    return new OperationDto(
        t.getId(),
        t.getAccountId(),
        resolveType(t),
        t.getOperationTime(),
        t.getMerchantName(),
        amount,
        category,
        t.isExcluded());
  }

  public Transaction toEntity(TransactionEvent event, Category category) {
    return new Transaction(
        UUID.randomUUID(),
        event.accountId(),
        event.operationTime(),
        event.trxnAmount(),
        event.amountCurrency(),
        event.accountAmount(),
        event.mcc(),
        event.merchantName(),
        category);
  }
}
