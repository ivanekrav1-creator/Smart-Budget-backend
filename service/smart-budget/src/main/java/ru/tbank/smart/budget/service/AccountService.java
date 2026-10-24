package ru.tbank.smart.budget.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.smart.budget.dao.account.entity.Account;
import ru.tbank.smart.budget.dao.account.repository.AccountRepository;
import ru.tbank.smart.budget.dao.user.entity.User;
import ru.tbank.smart.budget.exception.EntityAlreadyExistsException;
import ru.tbank.smart.budget.exception.EntityNotFoundException;
import ru.tbank.smart.budget.model.AccountCreateDto;

@Service
@Transactional(readOnly = true)
public class AccountService {

  private final AccountRepository accountRepository;
  private final UserService userService;

  public AccountService(AccountRepository accountRepository, UserService userService) {
    this.accountRepository = accountRepository;
    this.userService = userService;
  }

  @Transactional
  public Account createAccount(UUID userId, AccountCreateDto dto) {
    if (!userService.existsById(userId)) {
      throw new EntityNotFoundException("Пользователь с id: " + userId + " не найден");
    }
    if (accountRepository.existsById(dto.getId())) {
      throw new EntityAlreadyExistsException("Счет с id: " + dto.getId() + " уже существует");
    }

    User user = userService.getUserReference(userId);
    Account account =
        new Account(dto.getId(), user, dto.getCurrencyCode().getValue(), dto.getAmount());
    return accountRepository.save(account);
  }
}
