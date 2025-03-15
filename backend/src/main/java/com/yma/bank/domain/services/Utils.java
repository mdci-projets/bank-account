package com.yma.bank.domain.services;

import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.application.response.StatementLine;
import com.yma.bank.domain.Account;
import com.yma.bank.domain.DomainException;
import com.yma.bank.domain.Operation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Utils {

    /**
     * Convert the display operations of account to account statement (date, mount, currentBalance)
     *
     * @param account
     * @return
     */
    public static AccountStatementResponse generateAccountStatement(Account account) {
        Long accountId = account.getAccountId()
                .orElseThrow(() -> new DomainException("expected account ID not to be empty"));
        return new AccountStatementResponse(createStatementLineFromAccount(account), accountId);
    }

    private static List<StatementLine> createStatementLineFromAccount(
            Account account
    ) {
        List<StatementLine> statementLineList = new ArrayList<>();
        List<Operation> operationList = account.getOperationList();
        operationList.sort(Comparator.comparing(Operation::getTimestamp).reversed());
        BigDecimal balance = account.getBaseLineBalance().signum() == 0 ? account.calculateBalanceOperationsToDisplay() : account.getBaseLineBalance();

        if (!operationList.isEmpty()) {
            statementLineList.add(new StatementLine(operationList.get(0).getTimestamp(), operationList.get(0).getAmount(), balance));
            for (int i = 1; i < operationList.size(); i++) {
                balance = balance.subtract(operationList.get(i - 1).getAmount());
                statementLineList.add(new StatementLine(operationList.get(i).getTimestamp(), operationList.get(i).getAmount(), balance));
            }
        }

        return statementLineList;
    }
}
