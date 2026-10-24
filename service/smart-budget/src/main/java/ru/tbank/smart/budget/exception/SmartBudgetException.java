package ru.tbank.smart.budget.exception;

public class SmartBudgetException extends RuntimeException {
  public SmartBudgetException(String message) {
    super(message);
  }

  public SmartBudgetException(String message, Throwable cause) {
    super(message, cause);
  }
}
