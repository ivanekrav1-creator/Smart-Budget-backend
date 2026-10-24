package ru.tbank.smart.budget.api.mapper;

import org.springframework.stereotype.Component;
import ru.tbank.smart.budget.dao.account.entity.Account;
import ru.tbank.smart.budget.model.AccountDto;
import ru.tbank.smart.budget.model.Currency;

@Component
public class AccountMapper {
  public AccountDto toAccountDto(Account account) {
    return new AccountDto(
        account.getId(), Currency.fromValue(account.getCurrencyCode()), account.getAmount());
  }
}
