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
public class PayloadCreateMoneyTransferResponseDto implements Serializable {
    @Serial private static final long serialVersionUID = 20231122_7711L;

    private String moneyTransferId;
    private String status;
    private String direction;
    private Creditor creditor;
    private Debtor debtor;
    private String cro;
    private String uri;
    private String trn;
    private String description;
    private String createdDatetime;
    private String accountedDatetime ;
    private String debtorValueDate;
    private String creditorValueDate;
    private Amount amount;
    private Boolean isUrgent;
    private Boolean isInstant;
    private String feeType;
    private String feeAccountId;
    private List<Fee> fees;
    private Boolean hasTaxRelief;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creditor {
        private String name;
        private Account account;
        private Address address;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Debtor {
        private String name;
        private Account account;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Account {
        private String accountCode;
        private String bicCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String address;
        private String city;
        private String countryCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Amount {
        private Double debtorAmount;
        private String debtorCurrency;
        private Double creditorAmount;
        private String creditorCurrency;
        private String creditorCurrencyDate;
        private Double exchangeRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fee {
        private String feeCode;
        private String description;
        private Double amount;
        private String currency;
    }
}
