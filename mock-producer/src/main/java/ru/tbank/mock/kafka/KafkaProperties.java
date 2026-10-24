package ru.tbank.mock.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(String bootstrapServers, TopicConfig topic) {
  public record TopicConfig(
      String name, Integer partitions, Integer replicas, String retentionMs) {}
}
