package ru.tbank.smart.budget.dao.transaction.repository;

import static jakarta.persistence.criteria.JoinType.LEFT;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import ru.tbank.smart.budget.dao.transaction.entity.Transaction;

public class TransactionSpecification {

  public static Specification<Transaction> hasAccountIds(List<UUID> accountIds) {
    return (root, query, cb) ->
        CollectionUtils.isEmpty(accountIds)
            ? cb.conjunction()
            : root.get("accountId").in(accountIds);
  }

  public static Specification<Transaction> hasFrom(Instant from) {
    return (root, query, cb) ->
        from != null ? cb.greaterThanOrEqualTo(root.get("operationTime"), from) : cb.conjunction();
  }

  public static Specification<Transaction> hasTo(Instant to) {
    return (root, query, cb) ->
        to != null ? cb.lessThanOrEqualTo(root.get("operationTime"), to) : cb.conjunction();
  }

  public static Specification<Transaction> hasCategoryIds(List<Long> categoryIds) {
    return (root, query, cb) ->
        CollectionUtils.isEmpty(categoryIds)
            ? cb.conjunction()
            : root.get("category").get("id").in(categoryIds);
  }

  public static Specification<Transaction> withCategory() {
    return (root, query, cb) -> {
      root.fetch("category", LEFT);
      return cb.conjunction();
    };
  }
}
