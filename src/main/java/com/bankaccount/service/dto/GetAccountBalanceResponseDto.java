package com.bankaccount.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountBalanceResponseDto implements Serializable {
    @Serial private static final long serialVersionUID = 20230713_1899L;
    private String status;
    private List<String> error;
    private PayloadGetAccountBalanceResponseDto payload;
}
