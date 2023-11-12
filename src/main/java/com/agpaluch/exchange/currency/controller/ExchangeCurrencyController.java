package com.agpaluch.exchange.currency.controller;

import com.agpaluch.exchange.currency.api.CreateAccountApi;
import com.agpaluch.exchange.currency.model.AccountCreatedDTO;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;
import com.agpaluch.exchange.currency.service.CreateAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExchangeCurrencyController implements CreateAccountApi {

    private final CreateAccountService createAccountService;

    @Override
    public ResponseEntity<AccountCreatedDTO> createAccount(CreateAccountDTO createAccountDto) throws Exception {
        return ResponseEntity.ok(createAccountService.createAccount(createAccountDto));
    }

}
