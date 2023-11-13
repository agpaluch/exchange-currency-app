package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.Account;
import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.entities.CurrencyCode;
import com.agpaluch.exchange.currency.exceptions.AccountNotFoundException;
import com.agpaluch.exchange.currency.mapper.AccountBalanceMapper;
import com.agpaluch.exchange.currency.mapper.AccountBalanceMapperImpl;
import com.agpaluch.exchange.currency.mapper.CurrencyCodeMapperImpl;
import com.agpaluch.exchange.currency.model.AccountBalanceDTO;
import com.agpaluch.exchange.currency.model.CurrencyCodeDTO;
import com.agpaluch.exchange.currency.persistence.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountBalanceServiceTest {

    private AccountBalanceMapper mapper = new AccountBalanceMapperImpl(new CurrencyCodeMapperImpl());
    private AccountRepository accountRepository = mock(AccountRepository.class);
    private AccountBalanceService sut = new AccountBalanceService(mapper, accountRepository);

    @Test
    void getAccountBalance_returnsAllBalancesForAccount_existingAccountNumber() throws AccountNotFoundException {
        //given
        String accountNumber = UUID.randomUUID().toString();
        AccountBalance plnBalance = getAccountBalance(CurrencyCode.PLN, BigDecimal.TEN);
        AccountBalance usdBalance = getAccountBalance(CurrencyCode.USD, BigDecimal.valueOf(100.00));
        Account account = Account.builder().accountNumber(accountNumber).accountBalances(Arrays.asList(plnBalance, usdBalance)).build();

        when(accountRepository.findByAccountNumberFetchAccountBalances(accountNumber)).thenReturn(Optional.ofNullable(account));

        //when
        List<AccountBalanceDTO> result = sut.getAccountBalance(accountNumber);

        //then
        verify(accountRepository).findByAccountNumberFetchAccountBalances(eq(accountNumber));

        assertThat(result).hasSize(2);

        assertThat(result.stream()
                .filter(b -> CurrencyCodeDTO.PLN.equals(b.getCurrencyCode()) && plnBalance.getBalance().equals(b.getBalance()))
                .findFirst())
                .isPresent();

        assertThat(result.stream()
                .filter(b -> CurrencyCodeDTO.USD.equals(b.getCurrencyCode()) && usdBalance.getBalance().equals(b.getBalance()))
                .findFirst())
                .isPresent();
    }


    @Test
    void getAccountBalance_throwsAccountNotFoundException_nonExistingAccountNumber() {
        //given
        String accountNumber = UUID.randomUUID().toString();
        when(accountRepository.findByAccountNumberFetchAccountBalances(accountNumber)).thenReturn(Optional.empty());

        //when + then
        assertThatThrownBy(() -> sut.getAccountBalance(accountNumber))
                .isInstanceOf(AccountNotFoundException.class);
    }

    private AccountBalance getAccountBalance(CurrencyCode currencyCode, BigDecimal balance) {
        return AccountBalance.builder().currencyCode(currencyCode).balance(balance).build();
    }

}