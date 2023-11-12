package com.agpaluch.exchange.currency.exceptions;

public class ExchangeRatesNotFoundException extends Exception {

    public ExchangeRatesNotFoundException(String code) {
        super(String.format("Currency %s is not available.", code));
    }
}
