package ru.tbank.smart.budget.dao.notification.repository;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tbank.smart.budget.dao.notification.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
  @Query(
      "SELECT n.budget.id FROM Notification n WHERE n.user.id = :userId AND n.sentAt > :sentAfter")
  Set<UUID> findNotifiedBudgetIds(
      @Param("userId") UUID userId, @Param("sentAfter") Instant sentAfter);
}
