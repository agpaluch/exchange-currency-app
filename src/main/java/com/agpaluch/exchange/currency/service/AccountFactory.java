package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.Account;
import com.agpaluch.exchange.currency.entities.AccountBalance;

import java.util.UUID;

public class AccountFactory {

    public static Account createAccount(AccountBalance accountBalance) {
        return Account.builder()
                .accountBalance(accountBalance)
                .accountNumber(UUID.randomUUID().toString())
                .build();
    }

}
