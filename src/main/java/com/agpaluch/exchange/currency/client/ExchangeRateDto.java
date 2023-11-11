package com.agpaluch.exchange.currency.client;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
public class ExchangeRateDto {
    private String no;
    private LocalDate effectiveDate;
    private BigDecimal bid;
    private BigDecimal ask;
}

