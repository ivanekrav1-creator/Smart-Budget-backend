package ru.tbank.smart.budget.kafka.consumer;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(String bootstrapServers, Map<String, ConsumerProps> consumers) {
  public record ConsumerProps(
      String groupId,
      String autoOffsetReset,
      String topic,
      long retryInterval,
      long retryAttempts,
      int concurrency) {}
}
