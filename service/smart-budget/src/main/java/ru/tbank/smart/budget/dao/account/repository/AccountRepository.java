package ru.tbank.smart.budget.dao.account.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tbank.smart.budget.dao.account.entity.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {
  @Query("SELECT a FROM Account a JOIN FETCH a.user WHERE a.id = :id")
  Optional<Account> findByIdWithUser(@Param("id") UUID id);

  @Query("SELECT a.id FROM Account a WHERE a.user.id = :userId")
  List<UUID> findIdsByUserId(@Param("userId") UUID userId);

  @Query("SELECT (COUNT(a) > 0) FROM Account a WHERE a.id = :id AND a.user.id = :userId")
  boolean existsByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}
