package com.agpaluch.exchange.currency.exceptions;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(String accountNumber) {
        super(String.format("Account with number %s does not exist.", accountNumber));
    }
}
