package ru.tbank.smart.budget.api.record;

import java.math.BigDecimal;
import ru.tbank.smart.budget.dao.budget.entity.Budget;

public record BudgetWithAmount(Budget budget, BigDecimal actualAmount) {}
