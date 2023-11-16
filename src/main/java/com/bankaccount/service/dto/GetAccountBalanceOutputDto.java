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
public class GetAccountBalanceOutputDto implements Serializable {
    @Serial private static final long serialVersionUID = 20230713_9799L;
    private Double saldo;
}
