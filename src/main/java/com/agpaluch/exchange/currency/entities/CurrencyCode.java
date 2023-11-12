package com.agpaluch.exchange.currency.entities;

public enum CurrencyCode {
    PLN("PLN"),
    USD("USD");

    private final String code;

    CurrencyCode(String code) {
        this.code = code;
    }

}
