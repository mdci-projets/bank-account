package com.yma.bank.domain.services;

import com.yma.bank.domain.OperationHistory;
import java.util.List;

public interface OperationHistoryRepository {
    void save(OperationHistory operationHistory);
    List<OperationHistory> findByAccountId(Long accountId);
}
