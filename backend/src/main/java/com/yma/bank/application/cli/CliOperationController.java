package com.yma.bank.application.cli;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.domain.Account;
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
import java.util.List;
import java.util.Scanner;

@Component
@Profile("!test")
public class CliOperationController implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(CliOperationController.class);

    private static final Scanner scanner = new Scanner(System.in);

    private final AccountService accountService;

    private final StatementService statementService;

    private final ConsoleStatementPrinter consoleStatementPrinter;

    private final CliService cliService;

    private boolean running = false;
    private boolean alreadyExecuted = false;

    @Autowired
    public CliOperationController(AccountService accountService,
                                  ConsoleStatementPrinter consoleStatementPrinter,
                                  StatementService statementService,
                                  CliService cliService) {
        this.accountService = accountService;
        this.consoleStatementPrinter = consoleStatementPrinter;
        this.statementService = statementService;
        this.cliService = cliService;
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
        if (!alreadyExecuted) {
            deposit();
            withdraw();

            AccountStatementResponse accountStatement1 = getAccountStatement(654321L);
            consoleStatementPrinter.print(accountStatement1);

            AccountStatementResponse accountStatement2 = getAccountStatement(789123L);
            consoleStatementPrinter.print(accountStatement2);
            alreadyExecuted = true;
        }

        if (running) {
            LOG.warn("A CLI instance is already running.");
            return;
        }
        running = true;
        LOG.info("Welcome to the Banking CLI.");
        startInteractiveCli();
    }

    private void startInteractiveCli() {
        while (true) {
            displayTestAccounts();
            displayMenu();
            int choice = cliService.readInt("Your choice: ");

            if (choice < 1 || choice > 4) {
                System.out.println("Invalid choice! Please enter a number between 1 and 4.");
                continue;
            }

            switch (choice) {
                case 1 -> performDeposit();
                case 2 -> performWithdrawal();
                case 3 -> displayStatement();
                case 4 -> {
                    LOG.info("Returning to idle mode. CLI can be restarted via the REST API.");
                    running = false;
                    return;
                }
            }
        }
    }
    private void displayMenu() {
        System.out.println("\n===== Banking CLI Menu =====");
        System.out.println("1. Deposit money");
        System.out.println("2. Withdraw money");
        System.out.println("3. Display account statement");
        System.out.println("4. Exit");
    }

    private void displayTestAccounts() {
        try {
            List<Account> accounts = accountService.getAllAccounts(LocalDateTime.now());
            System.out.println("\n**Test Accounts Available:**");
            if (accounts.isEmpty()) {
                System.out.println("⚠ No test accounts found. Please create an account first.");
            } else {
                for (Account account : accounts) {
                    System.out.println("🔹 Account ID: " + account.getAccountId().orElse(null) +
                            " | Balance: " + account.getBaseLineBalance());
                }
            }
        } catch (Exception e) {
            LOG.error("Error fetching test accounts: {}", e.getMessage());
            System.out.println("❌ Error retrieving test accounts.");
        }
    }

    private void performDeposit() {
        Long accountId = cliService.readLong("Enter account ID: ");
        BigDecimal amount = cliService.readBigDecimal("Enter deposit amount: ");

        try {
            createOperation(accountId, amount, OperationTypeEnum.DEPOSIT);
            System.out.println("✅ Deposit of " + amount + " successfully made to account " + accountId);
        } catch (Exception e) {
            LOG.error("Deposit failed: {}", e.getMessage());
            System.out.println("❌ Error: Could not complete deposit. Please try again.");
        }
    }

    private void performWithdrawal() {
        Long accountId = cliService.readLong("Enter account ID: ");
        BigDecimal amount = cliService.readBigDecimal("Enter withdrawal amount: ");

        try {
            createOperation(accountId, amount, OperationTypeEnum.WITHDRAWAL);
            System.out.println("✅ Withdrawal of " + amount + " successfully made from account " + accountId);
        } catch (Exception e) {
            LOG.error("Withdrawal failed: {}", e.getMessage());
            System.out.println("❌ Error: Could not complete withdrawal. Please try again.");
        }
    }

    private void displayStatement() {
        Long accountId = cliService.readLong("Enter account ID: ");

        try {
            AccountStatementResponse statement = statementService.generateAccountStatement(accountId, LocalDateTime.now().minusDays(10));
            consoleStatementPrinter.print(statement);
            System.out.println("✅ Account statement displayed successfully.");
        } catch (Exception e) {
            LOG.error("Error displaying statement: {}", e.getMessage());
            System.out.println("❌ Error: Could not retrieve account statement.");
        }
    }
}
