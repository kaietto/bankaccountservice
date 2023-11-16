package com.bankaccount.service.dto;

import java.util.List;

@lombok.Data
@lombok.Builder
public class ErrorDto {
    private String status;
    private List<ErrorDetailDto> errors;
    private String payload;
}
