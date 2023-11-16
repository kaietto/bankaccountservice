package com.bankaccount.service.dto;

@lombok.Data
@lombok.Builder
public class ExceptionDto {
    private String errorCode;
    private String internalDescription;
}
