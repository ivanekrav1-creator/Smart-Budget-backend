package ru.tbank.smart.budget.record;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import ru.tbank.smart.budget.model.OperationType;

public record OperationRecord(
    UUID userId,
    List<UUID> accountIds,
    UUID budgetId,
    OperationType type,
    Instant from,
    Instant to) {}
