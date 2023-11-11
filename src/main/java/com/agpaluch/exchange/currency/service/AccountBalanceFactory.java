package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.exceptions.InvalidBalanceException;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;

import java.math.BigDecimal;

public class AccountBalanceFactory {

    public static AccountBalance createAccountBalance(CreateAccountDTO createAccountDto) throws InvalidBalanceException {
        return AccountBalance.builder()
                .plnBalance(getInitialBalancePln(createAccountDto))
                .build();
    }

    private static BigDecimal getInitialBalancePln(CreateAccountDTO createAccountDto) throws InvalidBalanceException {
        double initialBalancePln = createAccountDto.getInitialBalancePln();

        if (initialBalancePln <= 0) {
            throw new InvalidBalanceException("Initial PLN balance must be a positive number.");
        }

        return BigDecimal.valueOf(initialBalancePln);
    }
}
