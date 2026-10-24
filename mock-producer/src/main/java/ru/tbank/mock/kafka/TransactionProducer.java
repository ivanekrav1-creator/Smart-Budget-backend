package ru.tbank.mock.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tbank.mock.dto.TransactionEvent;
import ru.tbank.mock.generator.TransactionGenerator;

@Service
@ConditionalOnProperty(prefix = "mock.generator", name = "enabled", havingValue = "true")
public class TransactionProducer {
  private static final Logger log = LoggerFactory.getLogger(TransactionProducer.class);

  private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;
  private final TransactionGenerator generator;
  private final KafkaProperties kafkaProperties;

  public TransactionProducer(
      KafkaTemplate<String, TransactionEvent> kafkaTemplate,
      TransactionGenerator generator,
      KafkaProperties kafkaProperties) {
    this.kafkaTemplate = kafkaTemplate;
    this.generator = generator;
    this.kafkaProperties = kafkaProperties;
  }

  @Scheduled(fixedDelayString = "${mock.generator.rate-ms}")
  public void sendTransaction() {
    TransactionEvent event = generator.generate();
    String targetTopic = kafkaProperties.topic().name();
    kafkaTemplate
        .send(targetTopic, event)
        .whenComplete(
            (result, ex) -> {
              if (ex != null) {
                log.error("Failed to send event to topic {}: {}", targetTopic, event, ex);
              } else {
                log.info(
                    "Sent event to topic {} partition {} offset {}: {}",
                    targetTopic,
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset(),
                    event);
              }
            });
  }
  ;
}
