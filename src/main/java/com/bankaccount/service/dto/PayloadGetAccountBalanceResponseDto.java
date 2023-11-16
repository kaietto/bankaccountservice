package com.bankaccount.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadGetAccountBalanceResponseDto implements Serializable {
    @Serial private static final long serialVersionUID = 20231116_2222L;
    private String date;
    private Double balance;
    private Double availableBalance;
    private String currency;
}
