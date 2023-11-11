package com.agpaluch.exchange.currency.client;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BidAndAskPriceDto {
    private String table;
    private String currency;
    private String code;
    private List<ExchangeRateDto> rates;
}

