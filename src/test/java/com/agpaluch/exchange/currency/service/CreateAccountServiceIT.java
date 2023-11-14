package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.MvcTestUtils;
import com.agpaluch.exchange.currency.model.AccountBalanceDTO;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;
import com.agpaluch.exchange.currency.model.CurrencyCodeDTO;
import com.agpaluch.exchange.currency.persistence.AccountRepository;
import com.agpaluch.exchange.currency.persistence.CustomerRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
class CreateAccountServiceIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;

    private static Stream<Arguments> provideCreateAccountDtoWithInvalidInitialPlnBalance() {
        return Stream.of(
                Arguments.of(new CreateAccountDTO()
                        .firstName("testFirstName")
                        .lastName("testLastName")
                        .initialBalance(new AccountBalanceDTO().balance(BigDecimal.valueOf(-10.20)).currencyCode(CurrencyCodeDTO.PLN))),
                Arguments.of(new CreateAccountDTO()
                        .firstName("testFirstName")
                        .lastName("testLastName")
                        .initialBalance(new AccountBalanceDTO().balance(BigDecimal.valueOf(0.0)).currencyCode(CurrencyCodeDTO.PLN))),
                Arguments.of(new CreateAccountDTO()
                        .firstName("testFirstName")
                        .lastName("testLastName")
                        .initialBalance(new AccountBalanceDTO().balance(BigDecimal.valueOf(10.0)).currencyCode(CurrencyCodeDTO.USD)))
        );
    }

    private static Stream<Arguments> provideInvalidCreateAccountDto() {
        return Stream.of(
                Arguments.of(new CreateAccountDTO()
                        .lastName("testLastName")
                        .initialBalance(new AccountBalanceDTO().balance(BigDecimal.valueOf(100.00)).currencyCode(CurrencyCodeDTO.PLN))),
                Arguments.of(new CreateAccountDTO()
                        .firstName("testFirstName")
                        .lastName(null)
                        .initialBalance(new AccountBalanceDTO().balance(BigDecimal.valueOf(100.00)).currencyCode(CurrencyCodeDTO.PLN))),
                Arguments.of(new CreateAccountDTO()
                        .firstName("testFirstName")
                        .lastName("testLastName")
                        .initialBalance(null)),
                Arguments.of(new CreateAccountDTO()
                        .firstName("testFirstName")
                        .lastName(null)
                        .initialBalance(new AccountBalanceDTO().balance(null).currencyCode(CurrencyCodeDTO.PLN)))
        );
    }

    @Test
    void createAccount_createsCustomerAndAccountWithInitialBalance_correctRequestBody() throws Exception {
        //given
        CreateAccountDTO createAccountDto = new CreateAccountDTO()
                .firstName("testName")
                .lastName("lastName")
                .initialBalance(new AccountBalanceDTO().balance(BigDecimal.valueOf(100.00)).currencyCode(CurrencyCodeDTO.PLN));

        //when
        ResultActions resultActions = mockMvc.perform(post("/createAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MvcTestUtils.asJson(createAccountDto)));

        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").isNotEmpty())
                .andReturn();

        String accountNumber = MvcTestUtils.getFromBody(mvcResult, "$.accountNumber");

        assertThat(customerRepository.findByLastName(createAccountDto.getLastName()))
                .isNotEmpty();

        assertThat(accountRepository.findByAccountNumber(accountNumber))
                .isPresent();
    }

    @ParameterizedTest
    @MethodSource("provideCreateAccountDtoWithInvalidInitialPlnBalance")
    void createAccount_returnsUnprocessableEntity_invalidInitialPlnBalanceInRequestBody(CreateAccountDTO createAccountDto) throws Exception {
        //given + when
        ResultActions resultActions = mockMvc.perform(post("/createAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MvcTestUtils.asJson(createAccountDto)));

        //then
        resultActions.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorMessage").isNotEmpty())
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCreateAccountDto")
    void createAccount_returnsBadRequest_emptyRequiredFieldsInRequestBody(CreateAccountDTO createAccountDto) throws Exception {
        //given + when
        ResultActions resultActions = mockMvc.perform(post("/createAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MvcTestUtils.asJson(createAccountDto)));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andReturn();
    }

}