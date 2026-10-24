package ru.tbank.mock.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionEvent(
    UUID accountId,
    Instant operationTime,
    BigDecimal trxnAmount,
    String amountCurrency,
    BigDecimal accountAmount,
    String mcc,
    String merchantName) {}
