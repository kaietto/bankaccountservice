package com.bankaccount.service.dto;

@lombok.Data
@lombok.Builder
public class ErrorDetailDto {
        private String code;
        private String description;
        private String params;
}
