package com.agpaluch.exchange.currency.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidBalanceException extends Exception {

    public InvalidBalanceException(String message) {
        super(message);
    }
}
