package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.exceptions.InvalidBalanceException;
import com.agpaluch.exchange.currency.model.AccountBalanceDTO;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;
import com.agpaluch.exchange.currency.model.CurrencyCodeDTO;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountBalanceValidatorTest {

    private static Stream<Arguments> provideInvalidInitialPlnBalance() {
        return Stream.of(
                Arguments.of(new CreateAccountDTO()
                                .firstName("testFirstName")
                                .lastName("testLastName")
                                .initialBalance(new AccountBalanceDTO().balance(BigDecimal.valueOf(-10.78)).currencyCode(CurrencyCodeDTO.PLN)),
                        new CreateAccountDTO()
                                .firstName("testFirstName")
                                .lastName("testLastName")
                                .initialBalance(new AccountBalanceDTO().balance(BigDecimal.valueOf(56.89)).currencyCode(CurrencyCodeDTO.USD)),
                        Arguments.of(new CreateAccountDTO()
                                .firstName("testFirstName")
                                .lastName("testLastName")
                                .initialBalance(new AccountBalanceDTO().balance(BigDecimal.valueOf(0.0)).currencyCode(CurrencyCodeDTO.PLN))
                        )));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInitialPlnBalance")
    void validateInitialBalance_throwsInvalidBalanceException_invalidInitialBalancePln(CreateAccountDTO createAccountDto) {
        //given + when + then
        assertThatThrownBy(() -> AccountBalanceValidator.validateInitialBalance(createAccountDto))
                .isInstanceOf(InvalidBalanceException.class);
    }
}