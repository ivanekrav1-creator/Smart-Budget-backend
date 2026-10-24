package ru.tbank.smart.budget.listener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.smart.budget.api.record.BudgetWithAmount;
import ru.tbank.smart.budget.dao.account.repository.AccountRepository;
import ru.tbank.smart.budget.dao.notification.entity.Notification;
import ru.tbank.smart.budget.dao.notification.repository.NotificationRepository;
import ru.tbank.smart.budget.dao.user.entity.User;
import ru.tbank.smart.budget.event.TransactionSavedEvent;
import ru.tbank.smart.budget.service.BudgetService;
import ru.tbank.smart.budget.service.EmailService;

@Component
public class TransactionSavedListener {
  private static final String EMAIL_SUBJECT = "Превышение бюджета: %s";
  private final String emailText;
  private final BigDecimal notificationThresholdPercent;
  private final Duration notificationInterval;
  private final AccountRepository accountRepository;
  private final BudgetService budgetService;
  private final EmailService emailService;
  private final NotificationRepository notificationRepository;
  private static final Logger log = LoggerFactory.getLogger(TransactionSavedListener.class);

  public TransactionSavedListener(
      AccountRepository accountRepository,
      BudgetService budgetService,
      EmailService emailService,
      @Value("${smart.budget.notification-threshold-percent}")
          BigDecimal notificationThresholdPercent,
      @Value(
              "#{'Бюджет превысил ' + ${smart.budget.notification-threshold-percent} + '% от лимита.'}")
          String emailText,
      NotificationRepository notificationRepository,
      @Value("${smart.budget.notification-interval}") Duration notificationInterval) {
    this.accountRepository = accountRepository;
    this.budgetService = budgetService;
    this.emailService = emailService;
    this.notificationRepository = notificationRepository;
    this.notificationInterval = notificationInterval;
    this.notificationThresholdPercent = notificationThresholdPercent;
    this.emailText = emailText;
  }

  @Async
  @EventListener
  @Transactional
  public void onTransactionSaved(TransactionSavedEvent event) {
    accountRepository
        .findByIdWithUser(event.transaction().getAccountId())
        .ifPresentOrElse(
            account -> {
              User user = account.getUser();
              List<BudgetWithAmount> budgets = budgetService.getBudgets(user.getId());

              if (budgets.isEmpty()) {
                return;
              }

              Set<UUID> notifiedBudgets =
                  notificationRepository.findNotifiedBudgetIds(
                      user.getId(), Instant.now().minus(notificationInterval));

              budgets.stream()
                  .filter(b -> isThresholdExceeded(b, notificationThresholdPercent))
                  .filter(b -> !notifiedBudgets.contains(b.budget().getId()))
                  .forEach(b -> processBudgetNotification(b, user));
            },
            () -> log.warn("Account not found for id: {}", event.transaction().getAccountId()));
  }

  private boolean isThresholdExceeded(BudgetWithAmount b, BigDecimal thresholdPercent) {
    BigDecimal percent =
        b.actualAmount()
            .divide(b.budget().getAmount(), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    return percent.compareTo(thresholdPercent) >= 0;
  }

  private void processBudgetNotification(BudgetWithAmount b, User user) {
    String errorMessage = null;
    String formatedSubject = String.format(EMAIL_SUBJECT, b.budget().getName());

    try {
      emailService.sendEmail(user.getEmail(), formatedSubject, emailText);
    } catch (Exception e) {
      errorMessage = ExceptionUtils.getStackTrace(e);
    } finally {
      notificationRepository.save(
          new Notification(user, b.budget(), user.getEmail(), errorMessage, Instant.now()));
    }
  }
}
