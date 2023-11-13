package com.agpaluch.exchange.currency.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@Builder
@ToString
public class ExchangeRateDto {
    private String no;
    private LocalDate effectiveDate;
    private BigDecimal bid;
    private BigDecimal ask;
}

