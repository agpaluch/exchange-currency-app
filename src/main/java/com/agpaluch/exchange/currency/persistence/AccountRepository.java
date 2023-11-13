package com.agpaluch.exchange.currency.persistence;

import com.agpaluch.exchange.currency.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    @Query("select account from Account account " +
            "join fetch account.accountBalances accountBalances " +
            "where account.accountNumber like :accountNumber")
    Optional<Account> findByAccountNumberFetchAccountBalances(String accountNumber);
}
