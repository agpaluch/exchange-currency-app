package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.entities.CurrencyCode;
import com.agpaluch.exchange.currency.exceptions.InvalidBalanceException;
import com.agpaluch.exchange.currency.model.AccountBalanceDTO;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;
import com.agpaluch.exchange.currency.model.CurrencyCodeDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AccountBalanceFactoryTest {

    @Test
    void createInitialPLNBalance_createsAccountBalance_validInitialBalancePln() throws InvalidBalanceException {
        //given
        BigDecimal initialBalance = BigDecimal.valueOf(100.97);
        CreateAccountDTO createAccountDto = new CreateAccountDTO()
                .firstName("testFirstName")
                .lastName("testLastName")
                .initialBalance(new AccountBalanceDTO().balance(initialBalance).currencyCode(CurrencyCodeDTO.PLN));

        //when
        AccountBalance result = AccountBalanceFactory.createInitialPLNBalance(createAccountDto);

        //then
        assertThat(result)
                .extracting("balance", "currencyCode")
                .containsExactly(initialBalance, CurrencyCode.PLN);
    }


}