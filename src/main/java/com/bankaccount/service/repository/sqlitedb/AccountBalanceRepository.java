package com.bankaccount.service.repository.sqlitedb;

import com.bankaccount.service.entity.sqlitedb.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {
    Optional<AccountBalance> findByDate(String date);

}
