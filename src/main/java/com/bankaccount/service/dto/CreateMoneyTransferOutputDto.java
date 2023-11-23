package com.bankaccount.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMoneyTransferOutputDto implements Serializable {
    @Serial private static final long serialVersionUID = 20230722_0101L;
    private String esito;
}
