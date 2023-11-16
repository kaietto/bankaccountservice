package com.bankaccount.service.exception;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bankaccount.service.dto.ExceptionDto;
import java.util.LinkedList;

@Log4j2
@ControllerAdvice
public class ExceptionController {
    
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    protected ResponseEntity<List<ExceptionDto>> handleExceptionInternal(Throwable ex) {
    
        log.error("Controller2Advice handleExceptionInternal ex={}", ex);
        final String status;
        
        if (ex instanceof BaseException)
            status = ((BaseException) ex).getHttpStatus();
        else
            status = HttpStatus.INTERNAL_SERVER_ERROR.name();
        
        final List<ExceptionDto> exceptionDTOList = buildExceptionDTOList(ex);
        final HttpStatus valueOf = HttpStatus.valueOf(status);
        
        return new ResponseEntity<>(exceptionDTOList, valueOf);
    }
    
    
    
    public List<ExceptionDto> buildExceptionDTOList(Throwable ex) {
        
        final List<ExceptionDto> exceptionDTOs = new LinkedList<>();
        String errorCode = BaseException.BACKENDERROREGENERICO;
        if (ex instanceof BaseException) {
            errorCode = ((BaseException) ex).getErrorCode();
        }
        ExceptionDto exception = ExceptionDto.builder()
                                             .errorCode(errorCode)
                                             .build();
        if(null != ex)
            exception.setInternalDescription(ex.getMessage());
        
        exceptionDTOs.add(exception);
        return exceptionDTOs;
    }
    
    
}
