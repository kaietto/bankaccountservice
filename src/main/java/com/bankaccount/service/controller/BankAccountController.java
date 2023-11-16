package com.bankaccount.service.controller;

import com.bankaccount.service.dto.ExceptionDto;
import com.bankaccount.service.dto.GetAccountBalanceOutputDto;
import com.bankaccount.service.dto.GetAccountBalanceResponseDto;
import com.bankaccount.service.dto.TokenResponseDto;
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

/**
 * @author Claudio Menin
 * @date 16/11/2023
 * Controller of REST Bank Account Services
 */
@Log4j2
@RestController
@RequestMapping("/api/interface")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    // Claudio: WS Documentation at -> http://localhost:8080/bankaccountservice/swagger-ui.html

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

}


