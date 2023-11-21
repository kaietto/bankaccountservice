package com.bankaccount.service.entity.sqlitedb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "accountbalance")
public class AccountBalance implements Serializable {
    @Serial private static final long serialVersionUID = 20231117_1234L;

    @Id
    private Long id;
    @Column(columnDefinition = "DATE")
    private String date;
    private double balance;
    private double availableBalance;
    private String currency;
}
