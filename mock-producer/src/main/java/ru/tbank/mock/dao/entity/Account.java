package ru.tbank.mock.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "account")
public class Account {

  @Id private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "currency_code", columnDefinition = "CHARACTER(3)", nullable = false)
  private String currencyCode;

  public UUID getId() {
    return id;
  }
}
