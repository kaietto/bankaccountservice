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
public class TransactionType {
    @JsonProperty("enumeration")
    private String enumeration;

    @JsonProperty("value")
    private String value;
}