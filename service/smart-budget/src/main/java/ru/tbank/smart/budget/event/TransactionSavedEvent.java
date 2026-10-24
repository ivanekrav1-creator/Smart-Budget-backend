package ru.tbank.smart.budget.event;

import ru.tbank.smart.budget.dao.transaction.entity.Transaction;

public record TransactionSavedEvent(Transaction transaction) {}
