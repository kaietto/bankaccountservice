package com.bankaccount.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCashAccountTransactionsOutputDto {
    private String status;
    private List<String> error;
    private TransactionsPayload payload;
}