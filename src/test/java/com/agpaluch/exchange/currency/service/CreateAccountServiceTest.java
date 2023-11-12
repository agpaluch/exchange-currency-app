package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.Customer;
import com.agpaluch.exchange.currency.exceptions.InvalidBalanceException;
import com.agpaluch.exchange.currency.model.AccountCreatedDTO;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;
import com.agpaluch.exchange.currency.persistence.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateAccountServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CreateAccountService sut;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @Test
    void createAccount_returnsAccountCreatedDto_validCreateAccountDto() throws InvalidBalanceException {
        //given
        CreateAccountDTO createAccountDto = new CreateAccountDTO()
                .firstName("testFirstName")
                .lastName("testLastName")
                .initialBalancePln(34.75);

        //when
        AccountCreatedDTO accountCreatedDto = sut.createAccount(createAccountDto);

        //then
        verify(customerRepository).save(customerCaptor.capture());
        assertThat(customerCaptor.getValue())
                .extracting("firstName", "lastName")
                .containsExactly(createAccountDto.getFirstName(), createAccountDto.getLastName());
        assertThat(accountCreatedDto.getAccountNumber())
                .isNotBlank();
    }

    @Test
    void createAccount_throwsInvalidBalanceException_invalidCreateAccountDto() {
        //given
        CreateAccountDTO createAccountDto = new CreateAccountDTO()
                .firstName("testFirstName")
                .lastName("testLastName")
                .initialBalancePln(-19.3);

        //when + then
        assertThatThrownBy(() -> sut.createAccount(createAccountDto))
                .isInstanceOf(InvalidBalanceException.class);
    }

}