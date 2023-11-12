package com.agpaluch.exchange.currency.mapper;


import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.entities.CurrencyCode;
import com.agpaluch.exchange.currency.model.AccountBalanceDTO;
import com.agpaluch.exchange.currency.model.CurrencyCodeDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccountBalanceMapper {

    AccountBalance map(AccountBalanceDTO accountBalanceDTO);

    @ValueMapping(source = "USD", target = "USD")
    @ValueMapping(source = "PLN", target = "PLN")
    CurrencyCode map(CurrencyCodeDTO currencyCode);

}
