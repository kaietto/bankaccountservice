package com.bankaccount.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("operationId")
    private String operationId;

    @JsonProperty("accountingDate")
    private String accountingDate;

    @JsonProperty("valueDate")
    private String valueDate;

    @JsonProperty("type")
    private TransactionType type;

    @JsonProperty("amount")
    private int amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("description")
    private String description;
}

