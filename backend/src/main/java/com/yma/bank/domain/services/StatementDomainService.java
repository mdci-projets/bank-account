package com.yma.bank.domain.services;

import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.application.response.StatementLine;
import com.yma.bank.domain.BaseOperation;
import com.yma.bank.domain.OperationHistory;
import com.yma.bank.domain.DomainException;
import com.yma.bank.domain.OperationTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatementDomainService {
    public AccountStatementResponse generateStatement(Long accountId, List<OperationHistory> operations, LocalDateTime fromDate) {
        // Séparer les opérations avant et après `fromDate`
        List<OperationHistory> operationsBefore = operations.stream()
                .filter(op -> op.getTimestamp().isBefore(fromDate))
                .collect(Collectors.toList());

        List<OperationHistory> operationsAfter = operations.stream()
                .filter(op -> !op.getTimestamp().isBefore(fromDate))
                .sorted(Comparator.comparing(BaseOperation::getTimestamp)) // Trie du plus ancien au plus récent
                .collect(Collectors.toList());

        if (operationsAfter.isEmpty()) {
            throw new DomainException("No transaction found after the specified date.");
        }

        // Calculer le solde avant `fromDate`
        BigDecimal initialBalance = calculateBalanceBefore(operationsBefore);

        return new AccountStatementResponse(createStatementLines(operationsAfter, initialBalance), accountId);
    }

    private BigDecimal calculateBalanceBefore(List<OperationHistory> operationsBefore) {
        return operationsBefore.stream()
                .map(op -> op.getOperationType() == OperationTypeEnum.WITHDRAWAL ? op.getAmount().negate() : op.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<StatementLine> createStatementLines(List<OperationHistory> operations, BigDecimal initialBalance) {
        BigDecimal balance = initialBalance;
        List<StatementLine> statementLines = new java.util.ArrayList<>();

        for (OperationHistory operation : operations) {
            if (operation.getOperationType() == OperationTypeEnum.WITHDRAWAL) {
                balance = balance.subtract(operation.getAmount().abs());
            } else {
                balance = balance.add(operation.getAmount().abs());
            }
            statementLines.add(new StatementLine(operation.getTimestamp(),
                    operation.getOperationType() == OperationTypeEnum.WITHDRAWAL ? operation.getAmount().negate() : operation.getAmount(),
                    balance));
        }

        statementLines.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp())); // Trie du plus récent au plus ancien
        return statementLines;
    }
}