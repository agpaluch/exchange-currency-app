package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.Account;
import com.agpaluch.exchange.currency.entities.AccountBalance;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AccountFactoryTest {

    @Test
    void createAccount_createsAccount_withAccountBalance() {
        //given
        AccountBalance accountBalance = AccountBalance.builder()
                .plnBalance(new BigDecimal("100.00"))
                .usdBalance(new BigDecimal("30.38"))
                .build();

        //when
        Account account = AccountFactory.createAccount(accountBalance);

        //then
        assertThat(account.getAccountBalance()).isEqualTo(accountBalance);
        assertThat(account.getAccountNumber()).isNotBlank();
    }

}
