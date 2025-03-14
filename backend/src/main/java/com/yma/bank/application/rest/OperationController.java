package com.yma.bank.application.rest;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.domain.OperationTypeEnum;
import com.yma.bank.domain.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/account")
public class OperationController {
    private final OperationService operationService;

    @Autowired
    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping(value = "/{accountId}/deposit", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deposit(@PathVariable final Long accountId, @RequestParam("amount") final BigDecimal amount) {
        operationService.sendMoney(new NewOperationRequest(accountId, amount, OperationTypeEnum.DEPOSIT));
    }

    @PostMapping(value = "/{accountId}/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE)
    void withdraw(@PathVariable final Long accountId, @RequestParam("amount") final BigDecimal amount) {
        operationService.sendMoney(new NewOperationRequest(accountId, amount, OperationTypeEnum.WITHDRAWAL));
    }

    @GetMapping(value = "/{accountId}/operations", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    AccountStatementResponse getAccountStatement(@PathVariable final Long accountId) {
        return operationService.getAccountStatement(accountId, LocalDateTime.now().minusDays(10));
    }
}
