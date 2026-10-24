package ru.tbank.smart.budget.dao.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import ru.tbank.smart.budget.dao.user.entity.User;

@Entity
@Table(name = "account")
public class Account {

  @Id private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "currency_code", columnDefinition = "CHARACTER(3)", nullable = false)
  private String currencyCode;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  protected Account() {}

  public Account(UUID id, User user, String currencyCode, BigDecimal amount) {
    this.id = id;
    this.user = user;
    this.currencyCode = currencyCode;
    this.amount = amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public UUID getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public BigDecimal getAmount() {
    return amount;
  }
}
