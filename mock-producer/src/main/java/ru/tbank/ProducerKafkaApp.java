package ru.tbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProducerKafkaApp {
  public static void main(String[] args) {
    SpringApplication.run(ProducerKafkaApp.class, args);
  }
}
