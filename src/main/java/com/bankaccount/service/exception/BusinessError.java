package com.bankaccount.service.exception;

import com.bankaccount.service.enumeration.BusinessMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessError implements Serializable {
    private static final long serialVersionUID = 20231115_2001L;

    private BusinessMessage code;
    private String message;
    private String detailedMessage;
    private Integer status;
}
