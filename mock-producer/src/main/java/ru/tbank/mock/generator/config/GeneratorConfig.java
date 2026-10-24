package ru.tbank.mock.generator.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tbank.mock.dao.repository.AccountRepository;
import ru.tbank.mock.dto.Merchant;
import ru.tbank.mock.generator.TransactionGenerator;

@Configuration
@EnableConfigurationProperties(GeneratorProperties.class)
public class GeneratorConfig {

  @Bean
  public Map<Boolean, List<Merchant>> merchantsByType(GeneratorProperties props) {
    if (props.merchants().isEmpty()) {
      throw new IllegalStateException("No merchants configured");
    }

    return props.merchants().stream()
        .collect(Collectors.partitioningBy(m -> m.mcc() == props.creditMcc()));
  }

  @Bean
  public TransactionGenerator transactionGenerator(
      GeneratorProperties props,
      AccountRepository accountRepository,
      Map<Boolean, List<Merchant>> merchantsByType) {
    return new TransactionGenerator(props, accountRepository, merchantsByType);
  }
}
