package com.yma.bank.application.cli;

import com.yma.bank.application.response.AccountStatementResponse;

public interface StatementPrinter {
    void print(AccountStatementResponse accountStatementResponse);
}
