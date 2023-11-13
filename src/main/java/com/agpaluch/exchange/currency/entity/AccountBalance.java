package com.agpaluch.exchange.currency.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalance extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;
    @Column(scale = 4, precision = 30)
    private BigDecimal balance;
}
