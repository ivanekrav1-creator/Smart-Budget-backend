package ru.tbank.smart.budget.dao.budget.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tbank.smart.budget.dao.budget.entity.Budget;
import ru.tbank.smart.budget.dao.budget.projection.BudgetWithAmountProjection;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {

  @Query(
      value =
          """
        SELECT
            b.id as id,
            b.name as name,
            b.amount as amount,
            b.currency as currency,
            b.from_date as fromDate,
            b.to_date as toDate,
            b.category_ids as categoryIds,
            COALESCE(SUM(t.amount_value), 0) as actualAmount
        FROM budget b
        LEFT JOIN account acc ON acc.user_id = b.user_id
        LEFT JOIN transactions t ON t.account_id = acc.id
            AND t.category_id = ANY(b.category_ids)
            AND t.operation_time BETWEEN b.from_date AND b.to_date
            AND t.is_excluded = FALSE
        WHERE b.user_id = :userId
        GROUP BY b.id
        """,
      nativeQuery = true)
  List<BudgetWithAmountProjection> findByUserIdNative(@Param("userId") UUID userId);

  Optional<Budget> findByUserIdAndName(UUID userId, String name);

  @Query(
      value =
          """
            SELECT DISTINCT c.id
            FROM category c
            INNER JOIN budget b ON c.id = ANY(b.category_ids)
            WHERE b.user_id = :userId AND b.id = :budgetId
            """,
      nativeQuery = true)
  List<Long> findCategoryIdsByUserIdAndBudgetId(
      @Param("userId") UUID userId, @Param("budgetId") UUID budgetId);
}
