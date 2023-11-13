package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.MvcTestUtils;
import com.agpaluch.exchange.currency.client.BidAndAskPriceDto;
import com.agpaluch.exchange.currency.client.ExchangeRateDto;
import com.agpaluch.exchange.currency.client.NbpApiClient;
import com.agpaluch.exchange.currency.entities.Account;
import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.entities.CurrencyCode;
import com.agpaluch.exchange.currency.exceptions.ExchangeRatesNotFoundException;
import com.agpaluch.exchange.currency.exceptions.InvalidBalanceException;
import com.agpaluch.exchange.currency.model.*;
import com.agpaluch.exchange.currency.persistence.AccountRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
class ExchangeCurrencyServiceIT {

    private static final BigDecimal bidPrice = BigDecimal.valueOf(4.0978);
    private static final BigDecimal askPrice = BigDecimal.valueOf(4.1806);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @SpyBean
    private CreateAccountService createAccountService;
    @MockBean
    private NbpApiClient nbpApiClient;

    @BeforeEach
    public void setUp() throws ExchangeRatesNotFoundException {
        BidAndAskPriceDto bidAndAskPriceDto = BidAndAskPriceDto.builder()
                .currency(CurrencyCode.USD.name())
                .rate(ExchangeRateDto.builder()
                        .no("218/C/NBP/2023")
                        .effectiveDate(LocalDate.of(2023, 10, 11))
                        .bid(bidPrice)
                        .ask(askPrice)
                        .build())
                .build();
        when(nbpApiClient.getBidAndAskPrice(eq("C"), anyString())).thenReturn(bidAndAskPriceDto);
    }


    @Test
    void exchangeCurrency_exchangesPLNToUSD_validAccountNumberAndBalances() throws Exception {
        //given
        BigDecimal initialBalancePLN = BigDecimal.valueOf(100.00);
        BigDecimal amountSourceCurrency = BigDecimal.valueOf(10.00);

        String accountNumber = createAccount(initialBalancePLN);

        ExchangeCurrencyDTO exchangeCurrencyDTO = new ExchangeCurrencyDTO().sourceCurrencyCode(CurrencyCodeDTO.PLN)
                .targetCurrencyCode(CurrencyCodeDTO.USD)
                .amountSourceCurrency(amountSourceCurrency);

        //when
        ResultActions resultActions = mockMvc.perform(post("/exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountNumber", accountNumber)
                .content(MvcTestUtils.asJson(exchangeCurrencyDTO)));

        //then
        resultActions.andExpect(status().isNoContent())
                .andReturn();

        Optional<Account> accountModified = accountRepository.findByAccountNumberFetchAccountBalances(accountNumber);
        assertThat(accountModified).isNotEmpty();
        assertThat(accountModified.get().getAccountBalances())
                .filteredOn("currencyCode", CurrencyCode.PLN)
                .filteredOn("balance", initialBalancePLN.subtract(amountSourceCurrency))
                .hasSize(1);

        assertThat(accountModified.get().getAccountBalances())
                .filteredOn("currencyCode", CurrencyCode.USD)
                .filteredOn("balance", amountSourceCurrency.multiply(BigDecimal.ONE.divide(askPrice, 4, RoundingMode.HALF_DOWN)))
                .hasSize(1);
    }

    @Test
    void exchangeCurrency_exchangesUSDToPLN_validAccountNumberAndBalances() throws Exception {
        //given
        BigDecimal initialBalancePLN = BigDecimal.valueOf(10.00);
        BigDecimal initialBalanceUSD = BigDecimal.valueOf(100.00);
        BigDecimal amountSourceCurrency = BigDecimal.valueOf(10.00);

        String accountNumber = createAccount(initialBalancePLN);

        Account account = accountRepository.findByAccountNumberFetchAccountBalances(accountNumber).get();
        account.addAccountBalance(AccountBalance.builder().currencyCode(CurrencyCode.USD).balance(BigDecimal.valueOf(100.00)).build());
        accountRepository.save(account);

        ExchangeCurrencyDTO exchangeCurrencyDTO = new ExchangeCurrencyDTO().sourceCurrencyCode(CurrencyCodeDTO.USD)
                .targetCurrencyCode(CurrencyCodeDTO.PLN)
                .amountSourceCurrency(amountSourceCurrency);

        //when
        ResultActions resultActions = mockMvc.perform(post("/exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountNumber", accountNumber)
                .content(MvcTestUtils.asJson(exchangeCurrencyDTO)));

        //then
        resultActions.andExpect(status().isNoContent())
                .andReturn();

        Optional<Account> accountModified = accountRepository.findByAccountNumberFetchAccountBalances(accountNumber);
        assertThat(accountModified).isNotEmpty();
        assertThat(accountModified.get().getAccountBalances())
                .filteredOn("currencyCode", CurrencyCode.USD)
                .filteredOn("balance", initialBalanceUSD.subtract(amountSourceCurrency))
                .hasSize(1);

        assertThat(accountModified.get().getAccountBalances())
                .filteredOn("currencyCode", CurrencyCode.PLN)
                .filteredOn("balance", initialBalancePLN.add(amountSourceCurrency.multiply(bidPrice)))
                .hasSize(1);
    }


    @Test
    void exchangeCurrency_returnsUnprocessableEntity_notSufficientSourceCurrencyPLN() throws Exception {
        //given
        BigDecimal initialBalancePLN = BigDecimal.valueOf(100.00);
        BigDecimal amountSourceCurrency = BigDecimal.valueOf(120.00);

        String accountNumber = createAccount(initialBalancePLN);

        ExchangeCurrencyDTO exchangeCurrencyDTO = new ExchangeCurrencyDTO().sourceCurrencyCode(CurrencyCodeDTO.PLN)
                .targetCurrencyCode(CurrencyCodeDTO.USD)
                .amountSourceCurrency(amountSourceCurrency);

        //when
        ResultActions resultActions = mockMvc.perform(post("/exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountNumber", accountNumber)
                .content(MvcTestUtils.asJson(exchangeCurrencyDTO)));

        //then
        resultActions.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorMessage").isNotEmpty())
                .andReturn();
    }

    @Test
    void exchangeCurrency_returnsUnprocessableEntity_notSufficientSourceCurrencyUSD() throws Exception {
        //given
        BigDecimal initialBalancePLN = BigDecimal.valueOf(100.00);
        BigDecimal initialBalanceUSD = BigDecimal.valueOf(10.00);
        BigDecimal amountSourceCurrency = BigDecimal.valueOf(20.00);

        String accountNumber = createAccount(initialBalancePLN);

        Account account = accountRepository.findByAccountNumberFetchAccountBalances(accountNumber).get();
        account.addAccountBalance(AccountBalance.builder().currencyCode(CurrencyCode.USD).balance(initialBalanceUSD).build());
        accountRepository.save(account);

        ExchangeCurrencyDTO exchangeCurrencyDTO = new ExchangeCurrencyDTO().sourceCurrencyCode(CurrencyCodeDTO.USD)
                .targetCurrencyCode(CurrencyCodeDTO.PLN)
                .amountSourceCurrency(amountSourceCurrency);

        //when
        ResultActions resultActions = mockMvc.perform(post("/exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountNumber", accountNumber)
                .content(MvcTestUtils.asJson(exchangeCurrencyDTO)));

        //then
        resultActions.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorMessage").isNotEmpty())
                .andReturn();
    }

    @Test
    void exchangeCurrency_returnsNotFound_accountWithGivenNumberDoesNotExist() throws Exception {
        //given
        BigDecimal amountSourceCurrency = BigDecimal.valueOf(20.00);

        ExchangeCurrencyDTO exchangeCurrencyDTO = new ExchangeCurrencyDTO().sourceCurrencyCode(CurrencyCodeDTO.PLN)
                .targetCurrencyCode(CurrencyCodeDTO.USD)
                .amountSourceCurrency(amountSourceCurrency);

        //when
        ResultActions resultActions = mockMvc.perform(post("/exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountNumber", "nonExistingAccountNumber")
                .content(MvcTestUtils.asJson(exchangeCurrencyDTO)));

        //then
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").isNotEmpty())
                .andReturn();
    }

    @Test
    void exchangeCurrency_returnsUnprocessableEntity_noRatesForCurrencyAvailable() throws Exception {
        //given
        when(nbpApiClient.getBidAndAskPrice(eq("C"), anyString())).thenThrow(ExchangeRatesNotFoundException.class);

        BigDecimal initialBalancePLN = BigDecimal.valueOf(100.00);
        BigDecimal amountSourceCurrency = BigDecimal.valueOf(120.00);

        String accountNumber = createAccount(initialBalancePLN);

        ExchangeCurrencyDTO exchangeCurrencyDTO = new ExchangeCurrencyDTO().sourceCurrencyCode(CurrencyCodeDTO.PLN)
                .targetCurrencyCode(CurrencyCodeDTO.USD)
                .amountSourceCurrency(amountSourceCurrency);

        //when
        ResultActions resultActions = mockMvc.perform(post("/exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountNumber", accountNumber)
                .content(MvcTestUtils.asJson(exchangeCurrencyDTO)));

        //then
        resultActions.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorMessage").isNotEmpty())
                .andReturn();
    }

    private String createAccount(BigDecimal initialBalancePLN) throws InvalidBalanceException {
        CreateAccountDTO createAccountDto = new CreateAccountDTO()
                .firstName("testName")
                .lastName("lastName")
                .initialBalance(new AccountBalanceDTO().balance(initialBalancePLN).currencyCode(CurrencyCodeDTO.PLN));

        AccountCreatedDTO accountCreatedDTO = createAccountService.createAccount(createAccountDto);
        return accountCreatedDTO.getAccountNumber();
    }


}