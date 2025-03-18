package com.yma.bank.domain.services;

import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.application.response.StatementLine;
import com.yma.bank.domain.Account;
import com.yma.bank.domain.DomainException;
import com.yma.bank.domain.Operation;
import com.yma.bank.domain.OperationTypeEnum;

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
            for (Operation operation : operationList) {
                if (operation.getOperationType() == OperationTypeEnum.WITHDRAWAL) {
                    balance = balance.subtract(operation.getAmount().abs());
                } else {
                    balance = balance.add(operation.getAmount().abs());
                }
                statementLineList.add(new StatementLine(operation.getTimestamp(),
                        operation.getOperationType() == OperationTypeEnum.WITHDRAWAL ? operation.getAmount().negate() : operation.getAmount(),
                        balance));
            }
        }

        return statementLineList;
    }
}
