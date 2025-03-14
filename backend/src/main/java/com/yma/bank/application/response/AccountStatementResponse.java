package com.yma.bank.application.response;

import lombok.Getter;

import java.util.List;

public class AccountStatementResponse {
    @Getter
    private final Long accountId;

    @Getter
    private final List<StatementLine> statementLineList;

    public AccountStatementResponse(List<StatementLine> statementLineList,
                                    Long accountId) {
        this.statementLineList = statementLineList;
        this.accountId = accountId;
    }
}
