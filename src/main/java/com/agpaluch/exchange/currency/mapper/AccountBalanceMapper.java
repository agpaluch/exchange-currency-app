package com.agpaluch.exchange.currency.mapper;


import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.model.AccountBalanceDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = CurrencyCodeMapper.class)
public interface AccountBalanceMapper {

    AccountBalance map(AccountBalanceDTO accountBalanceDTO);

    default List<AccountBalance> map(List<AccountBalanceDTO> accountBalanceDTO) {
        return accountBalanceDTO.stream().map(this::map).collect(Collectors.toList());
    }

}
