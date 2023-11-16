package com.bankaccount.service.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Claudio Menin
 * @date 16/11/2023
 * Business Logics of Bank Account Service project
 */

@Log4j2
@Service
public class BankAccountService {

    @Value("${endpoints.baseurl}")
    private String baseurl;

    public String checkHealth() {
        return "Bank Account Service project is operational";
    }



}

