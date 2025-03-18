package com.yma.bank.domain.services;


import com.yma.bank.application.response.AccountStatementResponse;

import java.time.LocalDateTime;

public interface StatementService {
    AccountStatementResponse generateAccountStatement(Long accountId, LocalDateTime fromDate);
}
