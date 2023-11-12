package com.agpaluch.exchange.currency.entities;

import lombok.*;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalance extends AbstractEntity {

    private CurrencyCode currencyCode;
    private BigDecimal balance;
}
