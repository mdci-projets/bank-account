package com.yma.bank.application.rest;

import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.domain.services.StatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/statement")
@Tag(name = "Banking Statements", description = "API to manage account statements")
public class StatementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementController.class);
    private final StatementService statementService;

    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    /**
     * Retrieve the account transaction history.
     *
     * @param accountId The ID of the account.
     * @param fromDateString Start date for the bank statement.
     * @return AccountStatementResponse containing the transaction history.
     */
    @Operation(summary = "Get account transaction history",
            description = "Retrieves the transaction history for a specified account.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transaction history retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Account not found")
            })
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountStatementResponse> getAccountStatement(
            @Parameter(description = "Bank account ID") @PathVariable Long accountId,
            @Parameter(description = "Start date for the bank statement") @RequestParam("fromDate") String fromDateString
            ) {

        LOGGER.info("Retrieving transaction history for account ID {}", accountId);

        AccountStatementResponse response = statementService.generateAccountStatement(accountId, fromDateString != null ? LocalDateTime.parse(fromDateString) : LocalDateTime.now().minusDays(10));

        LOGGER.info("Transaction history retrieved for account ID {}", accountId);
        return ResponseEntity.ok(response);
    }
}
