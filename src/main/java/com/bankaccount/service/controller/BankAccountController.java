package com.bankaccount.service.controller;

import com.bankaccount.service.dto.*;
import com.bankaccount.service.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

/**
 * @author Claudio Menin
 * @date 16/11/2023
 * Controller class for REST banking services
 * Handles HTTP requests and delegates business logics to BankAccountService
 */
@Log4j2
@RestController
@RequestMapping("/api/interface")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    // Claudio: External - Web Service Documentation at -> http://localhost:8080/bankaccountservice/swagger-ui.html

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "4XX", description = "Client errors", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))})
    })
    @Operation(summary = "Check service health")
    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() { return ResponseEntity.ok(bankAccountService.checkHealth()); }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponseDto.class))}),
            @ApiResponse(responseCode = "4XX", description = "Client errors", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))})
    })
    @Operation(summary = "Get DEMO JWT token")
    @GetMapping("/getDemoToken")
    public ResponseEntity<TokenResponseDto> getToken() { return ResponseEntity.ok(bankAccountService.getToken()); }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GetAccountBalanceResponseDto.class))}),
            @ApiResponse(responseCode = "4XX", description = "Client errors", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))})
    })
    @Operation(summary = "Retrieve account balance")
    @GetMapping("/getAccountBalance/{accountId}")
    //public ResponseEntity<GetAccountBalanceResponseDto> getBalance(@RequestHeader(required = true, name = "Authorization") String accessToken,
    public ResponseEntity<GetAccountBalanceOutputDto> getBalance(@PathVariable("accountId") Long accountId) {
        return ResponseEntity.ok(bankAccountService.getAccountBalance(accountId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CreateMoneyTransferOutputDto.class))}),
            @ApiResponse(responseCode = "4XX", description = "Client errors", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))})
    })
    @Operation(summary = "Create a new money transfer")
    @PostMapping("/createMoneyTransfer/{accountId}")
    public ResponseEntity<CreateMoneyTransferOutputDto> createMoneyTransfer(@PathVariable("accountId") Long accountId) throws URISyntaxException {
        return ResponseEntity.ok(bankAccountService.createMoneyTransfer(accountId));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GetCashAccountTransactionsOutputDto.class))}),
            @ApiResponse(responseCode = "4XX", description = "Client errors", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))})
    })
    @Operation(summary = "Retrieve cash account transactions")
    @GetMapping("/getCashAccountTransactions/{accountId}")
    public ResponseEntity<GetCashAccountTransactionsOutputDto> getCashAccountTransactions(@PathVariable("accountId") Long accountId) {
        return ResponseEntity.ok(bankAccountService.getCashAccountTransactions(accountId));
    }

}


