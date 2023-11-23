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
public class CreateMoneyTransferResponseDto implements Serializable {
    @Serial private static final long serialVersionUID = 20230722_7799L;
    private String status;
    private List<String> error;
    private PayloadCreateMoneyTransferResponseDto payload;
}
