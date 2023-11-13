package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.Account;
import com.agpaluch.exchange.currency.exceptions.AccountNotFoundException;
import com.agpaluch.exchange.currency.mapper.AccountBalanceMapper;
import com.agpaluch.exchange.currency.model.AccountBalanceDTO;
import com.agpaluch.exchange.currency.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountBalanceService {

    private final AccountBalanceMapper accountBalanceMapper;
    private final AccountRepository accountRepository;

    public List<AccountBalanceDTO> getAccountBalance(String accountNumber) throws AccountNotFoundException {
        Optional<Account> account = accountRepository.findByAccountNumberFetchAccountBalances(accountNumber);

        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountNumber);
        }

        return accountBalanceMapper.map(account.get().getAccountBalances());
    }
}
