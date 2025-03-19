package com.yma.bank.application.rest;

import com.yma.bank.application.cli.CliOperationController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/cli")
@Tag(name = "CLI Controller", description = "Endpoints for managing the interactive CLI mode")
public class CliController {
    private static final Logger LOG = LoggerFactory.getLogger(CliController.class);
    private final CliOperationController cliOperationController;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public CliController(CliOperationController cliOperationController) {
        this.cliOperationController = cliOperationController;
    }

    @Operation(summary = "Start the interactive CLI mode",
            description = "This endpoint launches the CLI mode inside a separate thread, allowing users to perform banking operations interactively.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CLI mode successfully started"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/start")
    public String startCli() {
        LOG.info("Starting CLI via REST API...");
        executorService.submit(() -> {
            try {
                cliOperationController.run("rest");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return "CLI started in REST mode.";
    }
}
