package com.bankaccount.service.utility;

import com.bankaccount.service.dto.BusinessErrorDto;
import com.bankaccount.service.enumeration.BusinessMessage;
import com.bankaccount.service.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ExceptionUtility {

    public static BusinessErrorDto serializeException(Throwable throwable) throws IOException {

        BusinessErrorDto error = new BusinessErrorDto();
        if (BusinessException.class.isAssignableFrom(throwable.getClass())) {
            //business exception
            BusinessException businessException = (BusinessException) throwable;
            error.setCode(businessException.getCode().name());
            error.setMessage(businessException.getMessage());
            error.setDetailedMessage(getStackTrace(businessException));
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return error;
        }

        if (throwable.getCause() != null && BusinessException.class.isAssignableFrom(throwable.getCause().getClass())) {
            //internal business exception
            BusinessException businessException = (BusinessException) throwable.getCause();
            error.setCode(businessException.getCode().name());
            error.setMessage(businessException.getMessage());
            error.setDetailedMessage(getStackTrace(businessException));
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return error;
        }

        //non business exception
        error.setCode(BusinessMessage.GENERICERROR.name());
        error.setMessage(throwable.getMessage());
        error.setDetailedMessage(getStackTrace(throwable));
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return error;
    }

    private static String getStackTrace(final Throwable aThrowable) throws IOException {
        try (
                Writer result = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(result)
        ) {
            aThrowable.printStackTrace(printWriter);
            return result.toString();
        }
    }
}
