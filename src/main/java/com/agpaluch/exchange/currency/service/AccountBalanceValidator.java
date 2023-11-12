package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.CurrencyCode;
import com.agpaluch.exchange.currency.exceptions.InvalidBalanceException;
import com.agpaluch.exchange.currency.mapper.CurrencyCodeMapper;
import com.agpaluch.exchange.currency.mapper.CurrencyCodeMapperImpl;
import com.agpaluch.exchange.currency.model.AccountBalanceDTO;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;

import java.math.BigDecimal;

public class AccountBalanceValidator {

    private static CurrencyCodeMapper currencyCodeMapper = new CurrencyCodeMapperImpl();

    public static void validateInitialBalance(CreateAccountDTO createAccountDto) throws InvalidBalanceException {

        AccountBalanceDTO initialBalance = createAccountDto.getInitialBalance();

        CurrencyCode currencyCode = currencyCodeMapper.map(initialBalance.getCurrencyCode());

        if (!CurrencyCode.PLN.equals(currencyCode)) {
            throw new InvalidBalanceException("Initial balance in PLN must be provided.");
        }

        if (initialBalance.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidBalanceException("Initial balance must be a positive number.");
        }

    }
}
