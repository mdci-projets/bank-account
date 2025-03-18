package com.yma.bank.domain.services;

import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.domain.OperationHistory;

import java.time.LocalDateTime;
import java.util.List;

public class StatementServiceImpl implements StatementService {
    private final OperationHistoryRepository operationHistoryRepository;
    private final StatementDomainService statementDomainService;

    public StatementServiceImpl(OperationHistoryRepository operationHistoryRepository, StatementDomainService statementDomainService) {
        this.operationHistoryRepository = operationHistoryRepository;
        this.statementDomainService = statementDomainService;
    }

    @Override
    public AccountStatementResponse generateAccountStatement(Long accountId, LocalDateTime fromDate) {
        List<OperationHistory> operations = operationHistoryRepository.findByAccountId(accountId);
        return statementDomainService.generateStatement(accountId, operations, fromDate);
    }
}