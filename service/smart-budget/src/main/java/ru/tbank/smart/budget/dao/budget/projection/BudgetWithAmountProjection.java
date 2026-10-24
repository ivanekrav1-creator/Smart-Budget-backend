package ru.tbank.smart.budget.dao.budget.projection;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface BudgetWithAmountProjection {
  UUID getId();

  String getName();

  BigDecimal getAmount();

  String getCurrency();

  Instant getFromDate();

  Instant getToDate();

  Long[] getCategoryIds();

  BigDecimal getActualAmount();
}
