package com.agpaluch.exchange.currency.exception;

public class ExchangeRatesNotFoundException extends Exception {

    public ExchangeRatesNotFoundException(String code) {
        super(String.format("Currency %s is not available.", code));
    }
}
