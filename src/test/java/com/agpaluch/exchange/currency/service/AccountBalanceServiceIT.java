package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.Account;
import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.entities.CurrencyCode;
import com.agpaluch.exchange.currency.persistence.AccountRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
class AccountBalanceServiceIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void accountBalance_fetchesAllBalancesForAccount_existingAccountNumber() throws Exception {
        //given
        String accountNumber = UUID.randomUUID().toString();
        AccountBalance plnBalance = getAccountBalance(CurrencyCode.PLN, BigDecimal.TEN);
        AccountBalance usdBalance = getAccountBalance(CurrencyCode.USD, BigDecimal.valueOf(100.00));

        Account account = Account.builder().accountNumber(accountNumber).accountBalances(Arrays.asList(plnBalance, usdBalance)).build();
        accountRepository.save(account);

        //when
        ResultActions resultActions = mockMvc.perform(get("/accountBalance")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountNumber", accountNumber));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();
    }

    @Test
    void accountBalance_returnsNotFound_nonExistingAccountNumber() throws Exception {
        //given
        String accountNumber = UUID.randomUUID().toString();

        //when
        ResultActions resultActions = mockMvc.perform(get("/accountBalance")
                .contentType(MediaType.APPLICATION_JSON)
                .param("accountNumber", accountNumber));

        //then
        resultActions.andExpect(status().isNotFound())
                .andReturn();
    }

    private AccountBalance getAccountBalance(CurrencyCode currencyCode, BigDecimal balance) {
        return AccountBalance.builder().currencyCode(currencyCode).balance(balance).build();
    }
}