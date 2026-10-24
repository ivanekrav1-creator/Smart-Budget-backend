package ru.tbank.mock.generator;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import ru.tbank.mock.dao.entity.Account;
import ru.tbank.mock.dao.repository.AccountRepository;
import ru.tbank.mock.dto.Merchant;
import ru.tbank.mock.dto.TransactionEvent;
import ru.tbank.mock.generator.config.GeneratorProperties;

public class TransactionGenerator {

  private static final Logger log = LoggerFactory.getLogger(TransactionGenerator.class);

  private final GeneratorProperties props;
  private final AccountRepository accountRepository;
  private final Random random = new Random();
  private final Map<Boolean, List<Merchant>> merchantsByType;

  private volatile Map<String, List<UUID>> accountIdsByCurrency;
  private volatile List<String> currencies;

  public TransactionGenerator(
      GeneratorProperties props,
      AccountRepository accountRepository,
      Map<Boolean, List<Merchant>> merchantsByType) {
    this.props = props;
    this.accountRepository = accountRepository;
    this.merchantsByType = merchantsByType;
    refreshAccounts();
  }

  @Scheduled(fixedDelayString = "${mock.generator.account-refresh-rate-ms}")
  public void refreshAccounts() {
    Map<String, List<UUID>> updated =
        props.currency().stream()
            .collect(
                Collectors.toMap(
                    currency -> currency,
                    currency ->
                        accountRepository.findByCurrencyCode(currency.trim()).stream()
                            .map(Account::getId)
                            .toList()));

    if (updated.values().stream().anyMatch(List::isEmpty)) {
      throw new IllegalStateException("For one or more currencies account not found");
    }

    this.accountIdsByCurrency = updated;
    this.currencies = new ArrayList<>(updated.keySet());
    log.info(
        "Refreshed account cache: {} currencies, {} total accounts",
        updated.size(),
        updated.values().stream().mapToInt(List::size).sum());
  }

  public TransactionEvent generate() {
    String currency = pickRandom(currencies);
    UUID accountId = pickRandom(accountIdsByCurrency.get(currency));

    boolean isCredit =
        random.nextDouble() < props.creditProbability() && !merchantsByType.get(true).isEmpty();

    Merchant merchant = pickRandom(merchantsByType.get(isCredit));
    BigDecimal amount = generateAmount(isCredit);

    return new TransactionEvent(
        accountId,
        Instant.now(),
        amount,
        currency,
        amount,
        String.valueOf(merchant.mcc()),
        merchant.name());
  }

  private BigDecimal generateAmount(boolean isCredit) {
    BigDecimal base = generateRandomAmount(props.minAmount(), props.maxAmount());
    return isCredit ? base : base.negate();
  }

  private BigDecimal generateRandomAmount(BigDecimal min, BigDecimal max) {
    return min.add(max.subtract(min).multiply(BigDecimal.valueOf(random.nextDouble())))
        .setScale(2, HALF_UP);
  }

  private <T> T pickRandom(List<T> list) {
    return list.get(random.nextInt(list.size()));
  }
}
