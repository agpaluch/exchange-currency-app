package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.exceptions.InvalidBalanceException;
import com.agpaluch.exchange.currency.mapper.AccountBalanceMapper;
import com.agpaluch.exchange.currency.model.AccountBalanceDTO;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;


public class AccountBalanceFactory {

    private AccountBalanceMapper accountBalanceMapper = new AccountBalanceMapperImpl();

    public static AccountBalance createAccountBalance(CreateAccountDTO createAccountDto) throws InvalidBalanceException {
        AccountBalanceDTO initialBalance = createAccountDto.getInitialBalance();

        if (initialBalance == null || initialBalance.getCurrencyCode() == null || initialBalance.getBalance() == null) {
            throw new InvalidBalanceException("Initial balance must be provided.");
        }

        double initialBalanceValue = initialBalance.getBalance();

        if (initialBalanceValue <= 0) {
            throw new InvalidBalanceException("Initial PLN balance must be a positive number.");
        }
        return null;

        //return accountBalanceMapper.map(initialBalance);
    }
}
