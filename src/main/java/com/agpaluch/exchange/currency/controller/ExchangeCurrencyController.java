package com.agpaluch.exchange.currency.controller;

import com.agpaluch.exchange.currency.api.AccountBalanceApi;
import com.agpaluch.exchange.currency.api.CreateAccountApi;
import com.agpaluch.exchange.currency.api.ExchangeApi;
import com.agpaluch.exchange.currency.exception.AccountNotFoundException;
import com.agpaluch.exchange.currency.exception.ExchangeRatesNotFoundException;
import com.agpaluch.exchange.currency.exception.InvalidBalanceException;
import com.agpaluch.exchange.currency.model.AccountBalanceDTO;
import com.agpaluch.exchange.currency.model.AccountCreatedDTO;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;
import com.agpaluch.exchange.currency.model.ExchangeCurrencyDTO;
import com.agpaluch.exchange.currency.service.AccountBalanceService;
import com.agpaluch.exchange.currency.service.CreateAccountService;
import com.agpaluch.exchange.currency.service.ExchangeCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ExchangeCurrencyController implements CreateAccountApi, ExchangeApi, AccountBalanceApi {

    private final CreateAccountService createAccountService;
    private final ExchangeCurrencyService exchangeCurrencyService;
    private final AccountBalanceService accountBalanceService;

    @Override
    public ResponseEntity<AccountCreatedDTO> createAccount(CreateAccountDTO createAccountDto) throws InvalidBalanceException {
        return ResponseEntity.ok(createAccountService.createAccount(createAccountDto));
    }

    @Override
    public ResponseEntity<Void> exchangeCurrency(String accountNumber, ExchangeCurrencyDTO exchangeCurrencyDTO) throws AccountNotFoundException, InvalidBalanceException, ExchangeRatesNotFoundException {
        exchangeCurrencyService.exchangeCurrency(accountNumber, exchangeCurrencyDTO);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AccountBalanceDTO>> getAccountBalance(String accountNumber) throws AccountNotFoundException {
        List<AccountBalanceDTO> accountBalances = accountBalanceService.getAccountBalance(accountNumber);
        return ResponseEntity.ok().body(accountBalances);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

}
