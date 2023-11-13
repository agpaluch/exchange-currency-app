package com.agpaluch.exchange.currency.controller;

import com.agpaluch.exchange.currency.api.CreateAccountApi;
import com.agpaluch.exchange.currency.api.ExchangeApi;
import com.agpaluch.exchange.currency.exceptions.AccountNotFoundException;
import com.agpaluch.exchange.currency.exceptions.ExchangeRatesNotFoundException;
import com.agpaluch.exchange.currency.exceptions.InvalidBalanceException;
import com.agpaluch.exchange.currency.model.AccountCreatedDTO;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;
import com.agpaluch.exchange.currency.model.ExchangeCurrencyDTO;
import com.agpaluch.exchange.currency.service.CreateAccountService;
import com.agpaluch.exchange.currency.service.ExchangeCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ExchangeCurrencyController implements CreateAccountApi, ExchangeApi {

    private final CreateAccountService createAccountService;
    private final ExchangeCurrencyService exchangeCurrencyService;

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
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

}
