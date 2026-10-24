package ru.tbank.smart.budget.kafka.consumer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.tbank.smart.budget.kafka.consumer.dto.TransactionEvent;

@EnableKafka
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConsumerConfig {

  private final Logger log = LoggerFactory.getLogger(KafkaConsumerConfig.class);
  private static final String TRANSACTION_CONSUMER = "transaction-consumer";

  @Bean("transactionListenerFactory")
  public ConcurrentKafkaListenerContainerFactory<String, TransactionEvent>
      transactionListenerFactory(KafkaProperties props) {
    return createContainerFactory(props, TRANSACTION_CONSUMER, TransactionEvent.class);
  }

  private <T> ConcurrentKafkaListenerContainerFactory<String, T> createContainerFactory(
      KafkaProperties props, String consumerName, Class<T> eventType) {

    var config = props.consumers().get(consumerName);

    var factory = new ConcurrentKafkaListenerContainerFactory<String, T>();
    factory.setConsumerFactory(createConsumerFactory(props, config, eventType));
    factory.setCommonErrorHandler(createErrorHandler(config));
    factory.setConcurrency(config.concurrency());
    return factory;
  }

  private <T> ConsumerFactory<String, T> createConsumerFactory(
      KafkaProperties props, KafkaProperties.ConsumerProps config, Class<T> eventType) {

    Map<String, Object> kafkaProps = new HashMap<>();
    kafkaProps.put(BOOTSTRAP_SERVERS_CONFIG, props.bootstrapServers());
    kafkaProps.put(GROUP_ID_CONFIG, config.groupId());
    kafkaProps.put(AUTO_OFFSET_RESET_CONFIG, config.autoOffsetReset());
    kafkaProps.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    kafkaProps.put(VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    kafkaProps.put(VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(
        kafkaProps,
        new StringDeserializer(),
        new ErrorHandlingDeserializer<>(new JacksonJsonDeserializer<>(eventType, false)));
  }

  private DefaultErrorHandler createErrorHandler(KafkaProperties.ConsumerProps config) {
    var backOff = new FixedBackOff(config.retryInterval(), config.retryAttempts());

    return new DefaultErrorHandler(
        (record, ex) ->
            log.error(
                "All retries exhausted, skipping record at offset={}, key={}",
                record.offset(),
                record.key(),
                ex),
        backOff);
  }
}
