package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.Account;
import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.entities.CurrencyCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class AccountFactoryTest {

    @Test
    void createAccount_createsAccount_withAccountBalance() {
        //given
        AccountBalance accountBalance = AccountBalance.builder()
                .currencyCode(CurrencyCode.PLN)
                .balance(new BigDecimal("100.00"))
                .build();

        //when
        Account account = AccountFactory.createAccount(Collections.singletonList(accountBalance));

        //then
        assertThat(account.getAccountBalances()).hasSize(1);
        assertThat(account.getAccountBalances()).contains(accountBalance);
        assertThat(account.getAccountNumber()).isNotBlank();
    }

}
