package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.exceptions.InvalidBalanceException;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountBalanceFactoryTest {

    private static Stream<Arguments> provideInvalidInitialPlnBalance() {
        return Stream.of(
                Arguments.of(new CreateAccountDTO()
                                .firstName("testFirstName")
                                .lastName("testLastName")
                                .initialBalancePln(-10.20),
                        Arguments.of(new CreateAccountDTO()
                                .firstName("testFirstName")
                                .lastName("testLastName")
                                .initialBalancePln(0.0))
                ));
    }

    @Test
    void createAccountBalance_createsAccountBalance_validInitialBalancePln() throws InvalidBalanceException {
        //given
        Double initialBalance = 100.97;
        CreateAccountDTO createAccountDto = new CreateAccountDTO()
                .firstName("testFirstName")
                .lastName("testLastName")
                .initialBalancePln(initialBalance);

        //when
        AccountBalance result = AccountBalanceFactory.createAccountBalance(createAccountDto);

        //then
        assertThat(result)
                .extracting("plnBalance")
                .isEqualTo(BigDecimal.valueOf(initialBalance));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInitialPlnBalance")
    void createAccountBalance_throwsInvalidBalanceException_invalidInitialBalancePln(CreateAccountDTO createAccountDto) {
        //given + when + then
        assertThatThrownBy(() -> AccountBalanceFactory.createAccountBalance(createAccountDto))
                .isInstanceOf(InvalidBalanceException.class);
    }

}