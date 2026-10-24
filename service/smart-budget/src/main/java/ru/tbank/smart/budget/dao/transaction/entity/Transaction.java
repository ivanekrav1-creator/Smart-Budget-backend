package ru.tbank.smart.budget.dao.transaction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import ru.tbank.smart.budget.dao.category.entity.Category;

@Entity
@Table(name = "transactions")
public class Transaction {

  @Id private UUID id;

  @Column(name = "account_id", nullable = false)
  private UUID accountId;

  @Column(name = "operation_time", nullable = false)
  private Instant operationTime;

  @Column(name = "amount_value", nullable = false)
  private BigDecimal amountValue;

  @Column(name = "amount_currency", columnDefinition = "CHARACTER(3)", nullable = false)
  private String amountCurrency;

  @Column(name = "amount_in_account_currency", nullable = false)
  private BigDecimal amountInAccountCurrency;

  @Column(name = "mcc", columnDefinition = "CHARACTER(4)")
  private String mcc;

  @Column(name = "merchant_name")
  private String merchantName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @Column(name = "is_excluded", nullable = false)
  private boolean isExcluded;

  protected Transaction() {}

  public Transaction(
      UUID id,
      UUID accountId,
      Instant operationTime,
      BigDecimal amountValue,
      String amountCurrency,
      BigDecimal amountInAccountCurrency,
      String mcc,
      String merchantName,
      Category category) {
    this.id = id;
    this.accountId = accountId;
    this.operationTime = operationTime;
    this.amountValue = amountValue;
    this.amountCurrency = amountCurrency;
    this.amountInAccountCurrency = amountInAccountCurrency;
    this.mcc = mcc;
    this.merchantName = merchantName;
    this.category = category;
  }

  public UUID getId() {
    return id;
  }

  public UUID getAccountId() {
    return accountId;
  }

  public Instant getOperationTime() {
    return operationTime;
  }

  public BigDecimal getAmountValue() {
    return amountValue;
  }

  public String getAmountCurrency() {
    return amountCurrency;
  }

  public BigDecimal getAmountInAccountCurrency() {
    return amountInAccountCurrency;
  }

  public String getMcc() {
    return mcc;
  }

  public String getMerchantName() {
    return merchantName;
  }

  public Category getCategory() {
    return category;
  }

  public boolean isExcluded() {
    return isExcluded;
  }

  public void setExcluded(boolean excluded) {
    this.isExcluded = excluded;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
}
