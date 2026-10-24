package ru.tbank.smart.budget.api.mapper;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import ru.tbank.smart.budget.api.record.BudgetWithAmount;
import ru.tbank.smart.budget.dao.budget.entity.Budget;
import ru.tbank.smart.budget.model.AmountDto;
import ru.tbank.smart.budget.model.BudgetCreatedDto;
import ru.tbank.smart.budget.model.BudgetDto;
import ru.tbank.smart.budget.model.BudgetDtoAmount;
import ru.tbank.smart.budget.model.Currency;

@Component
public class BudgetMapper {

  public BudgetCreatedDto toResponseCreatedDto(Budget budget) {
    Currency currency = Currency.fromValue(budget.getCurrency());
    AmountDto amountDto = new AmountDto(budget.getAmount(), currency);

    return new BudgetCreatedDto(
        budget.getId(),
        budget.getName(),
        amountDto,
        budget.getFromDate(),
        budget.getToDate(),
        budget.getCategoryIds());
  }

  public BudgetDto toResponseDto(BudgetWithAmount item) {
    Budget budget = item.budget();
    BigDecimal actual = item.actualAmount();
    Currency currency = Currency.fromValue(budget.getCurrency());
    AmountDto expectedDto = new AmountDto(budget.getAmount(), currency);
    AmountDto actualDto = new AmountDto(actual, currency);

    BudgetDtoAmount amountWrapper = new BudgetDtoAmount().expected(expectedDto).actual(actualDto);

    return new BudgetDto(
        budget.getId(),
        budget.getName(),
        amountWrapper,
        budget.getFromDate(),
        budget.getToDate(),
        budget.getCategoryIds());
  }
}
