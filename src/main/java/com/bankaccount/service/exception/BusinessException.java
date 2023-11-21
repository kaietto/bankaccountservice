package com.bankaccount.service.exception;

import com.bankaccount.service.enumeration.BusinessMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class BusinessException extends RuntimeException {
    @Serial private static final long serialVersionUID = 20210712_1547L;

    private BusinessMessage code;
    private BusinessError payload;

    public BusinessException(BusinessMessage code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(BusinessMessage code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public BusinessException(BusinessMessage code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public BusinessException(BusinessError payload) { this.payload = payload; }
}

