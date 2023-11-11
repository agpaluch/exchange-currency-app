package com.agpaluch.exchange.currency.service;

import com.agpaluch.exchange.currency.entities.Account;
import com.agpaluch.exchange.currency.entities.Customer;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;

import java.util.List;

public class CustomerFactory {

    public static Customer createCustomer(CreateAccountDTO createAccountDto, List<Account> accounts) {
        return Customer.builder()
                .firstName(createAccountDto.getFirstName())
                .lastName(createAccountDto.getLastName())
                .accounts(accounts)
                .build();
    }

}
