package ru.tbank.smart.budget.api.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.smart.budget.api.OperationsApi;
import ru.tbank.smart.budget.model.ExcludeOperationDto;
import ru.tbank.smart.budget.model.OperationDto;
import ru.tbank.smart.budget.model.OperationType;
import ru.tbank.smart.budget.model.UpdateOperationCategoryDto;
import ru.tbank.smart.budget.record.OperationRecord;
import ru.tbank.smart.budget.service.OperationService;

@RestController
public class OperationController implements OperationsApi {

  private final OperationService operationService;

  public OperationController(OperationService operationService) {
    this.operationService = operationService;
  }

  @Override
  @PreAuthorize("@securityService.isOwner(#userId)")
  public ResponseEntity<List<OperationDto>> getUserOperations(
      UUID userId,
      Instant from,
      List<UUID> accountId,
      UUID budgetId,
      OperationType type,
      Instant to) {
    OperationRecord filter = new OperationRecord(userId, accountId, budgetId, type, from, to);
    return ResponseEntity.ok(operationService.getOperations(filter));
  }

  @Override
  @PreAuthorize("@securityService.isOperationOwner(#operationId)")
  public ResponseEntity<Void> excludeOperation(
      UUID operationId, ExcludeOperationDto excludeOperationDto) {

    operationService.changeExclusionStatus(operationId, excludeOperationDto.getExcluded());

    return ResponseEntity.noContent().build();
  }

  @Override
  @PreAuthorize("@securityService.isOperationOwner(#operationId)")
  public ResponseEntity<Void> updateOperationCategory(
      UUID operationId, UpdateOperationCategoryDto dto) {
    operationService.updateCategory(operationId, dto.getCategoryId());
    return ResponseEntity.noContent().build();
  }
}
