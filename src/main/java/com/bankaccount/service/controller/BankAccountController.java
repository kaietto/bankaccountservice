package com.bankaccount.service.controller;

import com.bankaccount.service.dto.ExceptionDto;
import com.bankaccount.service.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Check service health")
    @GetMapping("/health")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))}),
            @ApiResponse(responseCode = "4XX", description = "Client errors", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class))})
    })
    public ResponseEntity<String> checkHealth() { return ResponseEntity.ok(bankAccountService.checkHealth()); }
    
}


