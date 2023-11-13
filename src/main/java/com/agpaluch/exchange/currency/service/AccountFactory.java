package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entity.Account;
import com.agpaluch.exchange.currency.entity.AccountBalance;

import java.util.List;
import java.util.UUID;

public class AccountFactory {

    public static Account createAccount(List<AccountBalance> accountBalance) {

        return Account.builder()
                .accountBalances(accountBalance)
                .accountNumber(UUID.randomUUID().toString())
                .build();
    }

}
