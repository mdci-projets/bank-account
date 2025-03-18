package com.yma.bank.application.rest;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.domain.OperationTypeEnum;
import com.yma.bank.domain.services.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST Controller for managing bank operations (deposit, withdrawal, transaction history).
 * Uses Swagger for API documentation and logging for monitoring.
 */
@RestController
@RequestMapping("/account")
@Tag(name = "Banking Operations", description = "API to manage account transactions")
public class OperationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationController.class);
    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    /**
     * Deposit money into an account.
     *
     * @param accountId The ID of the account to deposit into.
     * @param amount    The amount to deposit.
     * @return ResponseEntity confirming the transaction.
     */
    @Operation(summary = "Deposit money",
            description = "Adds a specified amount to a given account.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deposit successful"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Account not found")
            })
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> deposit(
            @Parameter(description = "Bank account ID") @PathVariable Long accountId,
            @Parameter(description = "Amount to deposit") @RequestParam("amount") BigDecimal amount) {

        LOGGER.info("Deposit request for account ID {} of amount {}", accountId, amount);

        operationService.sendMoney(new NewOperationRequest(accountId, amount, OperationTypeEnum.DEPOSIT));

        LOGGER.info("Deposit successful for account ID {}", accountId);
        return ResponseEntity.ok("Deposit successful");
    }

    /**
     * Withdraw money from an account.
     *
     * @param accountId The ID of the account to withdraw from.
     * @param amount    The amount to withdraw.
     * @return ResponseEntity confirming the transaction.
     */
    @Operation(summary = "Withdraw money",
            description = "Withdraws a specified amount from a given account.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "403", description = "Insufficient funds")
            })
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdraw(
            @Parameter(description = "Bank account ID") @PathVariable Long accountId,
            @Parameter(description = "Amount to withdraw") @RequestParam("amount") BigDecimal amount) {

        LOGGER.info("Withdrawal request for account ID {} of amount {}", accountId, amount);

        operationService.sendMoney(new NewOperationRequest(accountId, amount, OperationTypeEnum.WITHDRAWAL));

        LOGGER.info("Withdrawal successful for account ID {}", accountId);
        return ResponseEntity.ok("Withdrawal successful");
    }
}
