package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entity.AccountBalance;
import com.agpaluch.exchange.currency.mapper.AccountBalanceMapper;
import com.agpaluch.exchange.currency.mapper.AccountBalanceMapperImpl;
import com.agpaluch.exchange.currency.mapper.CurrencyCodeMapper;
import com.agpaluch.exchange.currency.mapper.CurrencyCodeMapperImpl;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;

public class AccountBalanceFactory {

    private static CurrencyCodeMapper currencyCodeMapper = new CurrencyCodeMapperImpl();
    private static AccountBalanceMapper accountBalanceMapper = new AccountBalanceMapperImpl(currencyCodeMapper);

    public static AccountBalance createInitialPLNBalance(CreateAccountDTO createAccountDto) {
        return accountBalanceMapper.map(createAccountDto.getInitialBalance());
    }

}
