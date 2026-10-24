package ru.tbank.smart.budget.api.controller;

import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.tbank.smart.budget.api.AccountsApi;
import ru.tbank.smart.budget.api.mapper.AccountMapper;
import ru.tbank.smart.budget.dao.account.entity.Account;
import ru.tbank.smart.budget.model.AccountCreateDto;
import ru.tbank.smart.budget.model.AccountDto;
import ru.tbank.smart.budget.service.AccountService;

@RestController
public class AccountController implements AccountsApi {

  private final AccountService accountService;
  private final AccountMapper accountMapper;

  public AccountController(AccountService accountService, AccountMapper accountMapper) {
    this.accountService = accountService;
    this.accountMapper = accountMapper;
  }

  @Override
  @PreAuthorize("@securityService.isOwner(#userId)")
  public ResponseEntity<AccountDto> createAccount(UUID userId, AccountCreateDto dto) {
    Account account = accountService.createAccount(userId, dto);
    AccountDto response = accountMapper.toAccountDto(account);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(account.getId())
            .toUri();
    return ResponseEntity.created(location).body(response);
  }
}
