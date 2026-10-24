package ru.tbank.smart.budget.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.tbank.smart.budget.kafka.consumer.dto.TransactionEvent;
import ru.tbank.smart.budget.recognition.CategoryRecognizer;
import ru.tbank.smart.budget.service.OperationService;

@Service
public class TransactionConsumer {

  private final OperationService operationService;
  private final CategoryRecognizer CategoryRecognizer;
  private final Logger log = LoggerFactory.getLogger(TransactionConsumer.class);

  public TransactionConsumer(
      OperationService operationService, CategoryRecognizer CategoryRecognizer) {
    this.operationService = operationService;
    this.CategoryRecognizer = CategoryRecognizer;
  }

  @KafkaListener(
      topics = "${spring.kafka.consumers.transaction-consumer.topic}",
      containerFactory = "transactionListenerFactory")
  public void handleTransaction(ConsumerRecord<String, TransactionEvent> record) {
    try {
      log.info(
          "Processing transaction: partition={}, offset={}, key={}",
          record.partition(),
          record.offset(),
          record.key());
      TransactionEvent event = record.value();
      operationService.create(event, CategoryRecognizer.resolve(event.mcc(), event.merchantName()));
    } catch (Exception e) {
      log.error("error processing record at offset {}: {}", record.offset(), e.getMessage());
      throw e;
    }
  }
}
