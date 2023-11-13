package com.agpaluch.exchange.currency.exception;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(String accountNumber) {
        super(String.format("Account with number %s does not exist.", accountNumber));
    }
}
