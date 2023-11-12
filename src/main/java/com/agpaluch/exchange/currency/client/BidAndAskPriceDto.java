package com.agpaluch.exchange.currency.client;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class BidAndAskPriceDto {
    private String table;
    private String currency;
    private String code;
    @Singular("rate")
    private List<ExchangeRateDto> rates;
}

