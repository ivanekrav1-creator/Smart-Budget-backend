package ru.tbank.smart.budget.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.tbank.smart.budget.api.record.BudgetWithAmount;
import ru.tbank.smart.budget.dao.budget.entity.Budget;
import ru.tbank.smart.budget.dao.budget.repository.BudgetRepository;
import ru.tbank.smart.budget.dao.user.entity.User;
import ru.tbank.smart.budget.exception.BadRequestException;
import ru.tbank.smart.budget.exception.EntityNotFoundException;
import ru.tbank.smart.budget.exception.ForbiddenException;
import ru.tbank.smart.budget.model.BudgetCreateDto;
import ru.tbank.smart.budget.model.BudgetUpdateDto;

@Service
@Transactional(readOnly = true)
public class BudgetService {

  private final BudgetRepository budgetRepository;
  private final UserService userService;
  private final CategoryService categoryService;

  public BudgetService(
      BudgetRepository budgetRepository, UserService userService, CategoryService categoryService) {
    this.budgetRepository = budgetRepository;
    this.userService = userService;
    this.categoryService = categoryService;
  }

  @Transactional
  public Budget createBudget(UUID userId, BudgetCreateDto dto) {

    validateDates(dto.getFromDate(), dto.getToDate());

    validateCategories(dto.getCategoryIds());

    if (!userService.existsById(userId)) {
      throw new EntityNotFoundException("Пользователь с id: " + userId + " не найден");
    }

    User user = userService.getUserReference(userId);

    Budget budget =
        new Budget(
            user,
            dto.getName(),
            dto.getAmount().getValue(),
            dto.getAmount().getCurrency().name(),
            dto.getFromDate(),
            dto.getToDate(),
            dto.getCategoryIds());

    return budgetRepository.save(budget);
  }

  public List<BudgetWithAmount> getBudgets(UUID userId) {

    if (!userService.existsById(userId)) {
      throw new EntityNotFoundException("Пользователь с id: " + userId + " не найден");
    }

    User user = userService.getUserReference(userId);

    return budgetRepository.findByUserIdNative(userId).stream()
        .map(
            p -> {
              Budget budget =
                  new Budget(
                      p.getId(),
                      user,
                      p.getName(),
                      p.getAmount(),
                      p.getCurrency(),
                      p.getFromDate(),
                      p.getToDate(),
                      List.of(p.getCategoryIds()));
              return new BudgetWithAmount(budget, p.getActualAmount());
            })
        .toList();
  }

  @Transactional
  public void deleteBudget(UUID userId, UUID budgetId) {
    if (!userService.existsById(userId)) {
      throw new EntityNotFoundException("Пользователь с id: " + userId + " не найден");
    }
    if (!budgetRepository.existsById(budgetId)) {
      throw new EntityNotFoundException("Бюджет с id: " + budgetId + " не найден");
    }
    budgetRepository.deleteById(budgetId);
  }

  @Transactional
  public void updateBudget(UUID userId, UUID budgetId, BudgetUpdateDto dto) {

    validateDates(dto.getFromDate(), dto.getToDate());

    validateCategories(dto.getCategoryIds());

    if (!userService.existsById(userId)) {
      throw new EntityNotFoundException("Пользователь с id: " + userId + " не найден");
    }

    Budget budget =
        budgetRepository
            .findById(budgetId)
            .orElseThrow(
                () -> new EntityNotFoundException("Бюджет с id: " + budgetId + " не найден"));

    if (!budget.getUser().getId().equals(userId)) {
      throw new ForbiddenException("Доступ запрещен: вы не являетесь владельцем данного бюджета");
    }

    updateBudgetFields(budget, dto);
  }

  private void validateCategories(List<Long> categoryIds) {
    if (CollectionUtils.isEmpty(categoryIds)) {
      return;
    }

    if (!categoryService.allIdsExist(categoryIds)) {
      throw new BadRequestException("Категория не найдена");
    }
  }

  private static void validateDates(Instant fromDate, Instant toDate) {
    if (fromDate != null && toDate != null && toDate.isBefore(fromDate)) {
      throw new BadRequestException("Дата окончания бюджета не может быть раньше даты начала");
    }
  }

  private static void updateBudgetFields(Budget budget, BudgetUpdateDto dto) {
    budget.setName(dto.getName());
    budget.setAmount(dto.getAmount().getValue());
    budget.setCurrency(dto.getAmount().getCurrency().name());
    budget.setFromDate(dto.getFromDate());
    budget.setToDate(dto.getToDate());
    budget.setCategoryIds(dto.getCategoryIds());
  }
}
