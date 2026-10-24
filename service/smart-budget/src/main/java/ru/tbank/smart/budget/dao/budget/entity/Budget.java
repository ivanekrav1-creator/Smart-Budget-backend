package ru.tbank.smart.budget.dao.budget.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.SqlTypes;
import ru.tbank.smart.budget.dao.user.entity.User;

@Entity
@Table(name = "budget")
public class Budget {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false, length = 3)
  private String currency;

  @Column(name = "from_date", nullable = false)
  private Instant fromDate;

  @Column(name = "to_date", nullable = false)
  private Instant toDate;

  @JdbcTypeCode(SqlTypes.ARRAY)
  @Column(name = "category_ids", columnDefinition = "bigint[]")
  private List<Long> categoryIds = new ArrayList<>();

  public Budget() {}

  public Budget(
      User user,
      String name,
      BigDecimal amount,
      String currency,
      Instant fromDate,
      Instant toDate,
      List<Long> categoryIds) {
    this.user = user;
    this.name = name;
    this.amount = amount;
    this.currency = currency;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.categoryIds = categoryIds != null ? List.copyOf(categoryIds) : List.of();
  }

  public Budget(
      UUID id,
      User user,
      String name,
      BigDecimal amount,
      String currency,
      Instant fromDate,
      Instant toDate,
      List<Long> categoryIds) {
    this.id = id;
    this.user = user;
    this.name = name;
    this.amount = amount;
    this.currency = currency;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.categoryIds = categoryIds != null ? List.copyOf(categoryIds) : List.of();
  }

  public UUID getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getCurrency() {
    return currency;
  }

  public Instant getFromDate() {
    return fromDate;
  }

  public Instant getToDate() {
    return toDate;
  }

  public List<Long> getCategoryIds() {
    return List.copyOf(categoryIds);
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public void setFromDate(Instant fromDate) {
    this.fromDate = fromDate;
  }

  public void setToDate(Instant toDate) {
    this.toDate = toDate;
  }

  public void setCategoryIds(List<Long> categoryIds) {
    this.categoryIds = categoryIds != null ? new ArrayList<>(categoryIds) : new ArrayList<>();
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    Budget budget = (Budget) o;
    return getId() != null && Objects.equals(getId(), budget.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
