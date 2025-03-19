package com.yma.bank.application.rest;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.application.response.AccountDTO;
import com.yma.bank.domain.OperationTypeEnum;
import com.yma.bank.domain.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for managing bank operations (deposit, withdrawal, transaction history).
 * Uses Swagger for API documentation and logging for monitoring.
 */
@RestController
@RequestMapping("/api/account")
@Tag(name = "Banking Operations", description = "API to manage account transactions")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
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

        accountService.sendMoney(new NewOperationRequest(accountId, amount, OperationTypeEnum.DEPOSIT));

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

        accountService.sendMoney(new NewOperationRequest(accountId, amount, OperationTypeEnum.WITHDRAWAL));

        LOGGER.info("Withdrawal successful for account ID {}", accountId);
        return ResponseEntity.ok("Withdrawal successful");
    }

    @Operation(summary = "Retrieve an account by its account ID",
            description = "Fetches an account based on the provided account ID and baseline date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{accountId}")
    public AccountDTO getAccount(
            @Parameter(description = "ID of the account to retrieve", example = "121")
            @PathVariable Long accountId,

            @Parameter(description = "Baseline date for account history",
                    example = "2025-03-16T14:00:00")
            @RequestParam(required = false) LocalDateTime baselineDate) {

        return accountService.getAccount(accountId, baselineDate != null ? baselineDate : LocalDateTime.now());
    }

    @Operation(
            summary = "Retrieve all accounts",
            description = "Returns a list of all accounts with their current balance. An optional parameter allows specifying a reference date for account history."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of accounts",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<AccountDTO> getAllAccounts(
            @Parameter(description = "Reference date for account history",
                    example = "2025-03-16T14:00:00")
            @RequestParam(required = false) LocalDateTime baselineDate) {

        return accountService.getAllAccounts(baselineDate != null ? baselineDate : LocalDateTime.now());
    }
}
