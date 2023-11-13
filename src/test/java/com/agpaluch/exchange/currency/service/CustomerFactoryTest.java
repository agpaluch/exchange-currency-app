package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.Account;
import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.entities.CurrencyCode;
import com.agpaluch.exchange.currency.entities.Customer;
import com.agpaluch.exchange.currency.model.AccountBalanceDTO;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;
import com.agpaluch.exchange.currency.model.CurrencyCodeDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerFactoryTest {

    @Test
    void createCustomer_createsCustomer_withAccount() {
        //given
        BigDecimal initialBalanceValue = BigDecimal.valueOf(13.4);
        CreateAccountDTO createAccountDto = new CreateAccountDTO()
                .firstName("testFirstName")
                .lastName("testLastName")
                .initialBalance(new AccountBalanceDTO().currencyCode(CurrencyCodeDTO.PLN).balance(initialBalanceValue));

        Account account = Account.builder()
                .accountBalance(AccountBalance.builder()
                        .currencyCode(CurrencyCode.PLN)
                        .balance(initialBalanceValue)
                        .build())
                .build();

        //when
        Customer customer = CustomerFactory.createCustomer(createAccountDto, Collections.singletonList(account));

        //then
        assertThat(customer)
                .extracting("firstName", "lastName")
                .containsExactly(createAccountDto.getFirstName(), createAccountDto.getLastName());

        assertThat(customer.getAccounts())
                .hasSize(1)
                .containsExactlyInAnyOrder(account);
    }
}