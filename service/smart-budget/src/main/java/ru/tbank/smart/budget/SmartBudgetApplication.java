package ru.tbank.smart.budget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartBudgetApplication {

  public static void main(String[] args) {
    SpringApplication.run(SmartBudgetApplication.class, args);
  }
}
