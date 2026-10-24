package ru.tbank.smart.budget.api.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.tbank.smart.budget.api.BudgetsApi;
import ru.tbank.smart.budget.api.mapper.BudgetMapper;
import ru.tbank.smart.budget.dao.budget.entity.Budget;
import ru.tbank.smart.budget.model.BudgetCreateDto;
import ru.tbank.smart.budget.model.BudgetCreatedDto;
import ru.tbank.smart.budget.model.BudgetDto;
import ru.tbank.smart.budget.model.BudgetUpdateDto;
import ru.tbank.smart.budget.service.BudgetService;

@RestController
public class BudgetController implements BudgetsApi {

  private final BudgetService budgetService;
  private final BudgetMapper budgetMapper;

  public BudgetController(BudgetService budgetService, BudgetMapper budgetMapper) {
    this.budgetService = budgetService;
    this.budgetMapper = budgetMapper;
  }

  @Override
  public ResponseEntity<BudgetCreatedDto> createBudget(
      UUID userId, BudgetCreateDto budgetCreateDto) {
    Budget budget = budgetService.createBudget(userId, budgetCreateDto);
    BudgetCreatedDto response = budgetMapper.toResponseCreatedDto(budget);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.getId())
            .toUri();
    return ResponseEntity.created(location).body(response);
  }

  @Override
  @PreAuthorize("@securityService.isOwner(#userId)")
  public ResponseEntity<List<BudgetDto>> getUserBudgets(UUID userId) {
    List<BudgetDto> budgets =
        budgetService.getBudgets(userId).stream().map(budgetMapper::toResponseDto).toList();
    return ResponseEntity.ok(budgets);
  }

  @Override
  @PreAuthorize("@securityService.isOwner(#userId)")
  public ResponseEntity<Void> deleteBudget(UUID userId, UUID budgetId) {
    budgetService.deleteBudget(userId, budgetId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PreAuthorize("@securityService.isOwner(#userId)")
  public ResponseEntity<Void> updateBudget(
      UUID userId, UUID budgetId, BudgetUpdateDto budgetUpdateDto) {

    budgetService.updateBudget(userId, budgetId, budgetUpdateDto);

    return ResponseEntity.noContent().build();
  }
}
