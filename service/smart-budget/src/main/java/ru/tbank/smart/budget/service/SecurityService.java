package ru.tbank.smart.budget.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.smart.budget.dao.account.repository.AccountRepository;
import ru.tbank.smart.budget.dao.transaction.entity.Transaction;
import ru.tbank.smart.budget.dao.transaction.repository.TransactionRepository;
import ru.tbank.smart.budget.dao.user.entity.User;
import ru.tbank.smart.budget.dao.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class SecurityService {

  private final UserRepository userRepository;
  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;

  public SecurityService(
      UserRepository userRepository,
      TransactionRepository transactionRepository,
      AccountRepository accountRepository) {
    this.userRepository = userRepository;
    this.transactionRepository = transactionRepository;
    this.accountRepository = accountRepository;
  }

  public String getCurrentEmail() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  public boolean isOwner(UUID userId) {
    String currentEmail = getCurrentEmail();

    return userRepository
        .findByEmail(currentEmail)
        .map(user -> user.getId().equals(userId))
        .orElse(false);
  }

  public boolean isOperationOwner(UUID operationId) {
    String currentEmail = getCurrentEmail();
    System.out.println("DEBUG currentEmail=[" + currentEmail + "] operationId=" + operationId);

    Optional<User> user = userRepository.findByEmail(currentEmail);
    System.out.println(
        "DEBUG userFound="
            + user.isPresent()
            + (user.isPresent() ? " userId=" + user.get().getId() : ""));

    if (user.isEmpty()) {
      return false;
    }

    Optional<Transaction> transactionOpt = transactionRepository.findById(operationId);
    System.out.println("DEBUG transactionFound=" + transactionOpt.isPresent());
    transactionOpt.ifPresent(t -> System.out.println("DEBUG accountId=" + t.getAccountId()));

    boolean result =
        transactionOpt
            .map(
                transaction ->
                    accountRepository.existsByIdAndUserId(
                        transaction.getAccountId(), user.get().getId()))
            .orElse(false);
    System.out.println("DEBUG existsByIdAndUserId result=" + result);

    return result;
  }
}
