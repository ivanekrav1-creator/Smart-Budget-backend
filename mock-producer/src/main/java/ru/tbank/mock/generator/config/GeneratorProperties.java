package ru.tbank.mock.generator.config;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.tbank.mock.dto.Merchant;

@ConfigurationProperties(prefix = "mock.generator")
public record GeneratorProperties(
    boolean enabled,
    long rateMs,
    long accountRefreshRateMs,
    BigDecimal minAmount,
    BigDecimal maxAmount,
    double creditProbability,
    int creditMcc,
    List<String> currency,
    List<Merchant> merchants) {}
