package ru.tbank.smart.budget.dao.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.proxy.HibernateProxy;
import ru.tbank.smart.budget.dao.budget.entity.Budget;
import ru.tbank.smart.budget.dao.user.entity.User;

@Entity
@Table(name = "notifications")
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "budget_id", nullable = false)
  private Budget budget;

  @Column(nullable = false)
  private String email;

  @Column(name = "error_message")
  private String errorMessage;

  @Column(name = "sent_at", nullable = false)
  private Instant sentAt;

  public Notification() {}

  public Notification(User user, Budget budget, String email, String errorMessage, Instant sentAt) {
    this.user = user;
    this.budget = budget;
    this.email = email;
    this.errorMessage = errorMessage;
    this.sentAt = sentAt;
  }

  public UUID getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public Budget getBudget() {
    return budget;
  }

  public String getEmail() {
    return email;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public Instant getSentAt() {
    return sentAt;
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
    Notification that = (Notification) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
