package com.yma.bank.application.cli;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.domain.OperationTypeEnum;
import com.yma.bank.domain.services.AccountService;
import com.yma.bank.domain.services.StatementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Profile("!test")
public class CliOperationController implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(CliOperationController.class);

    private final AccountService accountService;

    private final StatementService statementService;

    private final ConsoleStatementPrinter consoleStatementPrinter;

    @Autowired
    public CliOperationController(AccountService accountService, ConsoleStatementPrinter consoleStatementPrinter, StatementService statementService) {
        this.accountService = accountService;
        this.consoleStatementPrinter = consoleStatementPrinter;
        this.statementService = statementService;
    }

    public void deposit() {
        LOG.info("<<Deposit some money>>");
        createOperation(654321L, BigDecimal.valueOf(500L), OperationTypeEnum.DEPOSIT);
        createOperation(789123L, BigDecimal.valueOf(800L), OperationTypeEnum.DEPOSIT);
        createOperation(654321L, BigDecimal.valueOf(200L), OperationTypeEnum.DEPOSIT);
        createOperation(789123L, BigDecimal.valueOf(100L), OperationTypeEnum.DEPOSIT);
    }

    public void withdraw() {
        LOG.info("<<Withdraw some or all money>>");
        createOperation(654321L, BigDecimal.valueOf(300L), OperationTypeEnum.WITHDRAWAL);
        createOperation(789123L, BigDecimal.valueOf(200L), OperationTypeEnum.WITHDRAWAL);
        createOperation(654321L, BigDecimal.valueOf(100L), OperationTypeEnum.WITHDRAWAL);
        createOperation(789123L, BigDecimal.valueOf(100L), OperationTypeEnum.WITHDRAWAL);
    }

    private void createOperation(Long accountId, BigDecimal amount, OperationTypeEnum operationTypeEnum) {
        LOG.info(String.format("%s a amount of %s for my account %s", operationTypeEnum, amount, accountId));
        NewOperationRequest newOperationRequest = new NewOperationRequest(accountId, amount, operationTypeEnum);
        accountService.sendMoney(newOperationRequest);
    }

    public AccountStatementResponse getAccountStatement(Long accountId) {
        return statementService.generateAccountStatement(accountId, LocalDateTime.now().minusDays(10));
    }

    public void printAccountStatement(AccountStatementResponse accountStatementResponse, StatementPrinter printer) {
        printer.print(accountStatementResponse);
    }

    @Override
    public void run(String... args) throws Exception {
        deposit();
        withdraw();

        AccountStatementResponse accountStatement1 = getAccountStatement(654321L);
        consoleStatementPrinter.print(accountStatement1);

        AccountStatementResponse accountStatement2 = getAccountStatement(789123L);
        consoleStatementPrinter.print(accountStatement2);
    }
}
