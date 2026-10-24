package ru.tbank.mock.kafka;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import ru.tbank.mock.dto.TransactionEvent;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaProducerConfig {
  private final KafkaProperties kafkaProperties;

  public KafkaProducerConfig(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }

  @Bean
  public NewTopic transactionsTopic() {
    var topicConfig = kafkaProperties.topic();

    return TopicBuilder.name(topicConfig.name())
        .partitions(topicConfig.partitions())
        .replicas(topicConfig.replicas())
        .config("retention.ms", topicConfig.retentionMs())
        .build();
  }

  @Bean
  public ProducerFactory<String, TransactionEvent> producerFactory() {
    JacksonJsonSerializer<TransactionEvent> valueSerializer = new JacksonJsonSerializer<>();
    valueSerializer.setAddTypeInfo(false);

    return new DefaultKafkaProducerFactory<>(
        Map.of(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers()),
        new StringSerializer(),
        valueSerializer);
  }

  @Bean
  public KafkaTemplate<String, TransactionEvent> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
