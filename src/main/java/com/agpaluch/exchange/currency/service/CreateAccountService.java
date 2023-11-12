package com.agpaluch.exchange.currency.service;


import com.agpaluch.exchange.currency.entities.Account;
import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.entities.Customer;
import com.agpaluch.exchange.currency.exceptions.InvalidBalanceException;
import com.agpaluch.exchange.currency.model.AccountCreatedDTO;
import com.agpaluch.exchange.currency.model.CreateAccountDTO;
import com.agpaluch.exchange.currency.persistence.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateAccountService {

    private final CustomerRepository customerRepository;


    public AccountCreatedDTO createAccount(CreateAccountDTO createAccountDto) throws InvalidBalanceException {

        AccountBalanceValidator.validateInitialBalance(createAccountDto);

        AccountBalance accountBalance = AccountBalanceFactory.createInitialPLNBalance(createAccountDto);
        Account account = AccountFactory.createAccount(Collections.singletonList(accountBalance));
        Customer customer = CustomerFactory.createCustomer(createAccountDto, Collections.singletonList(account));

        customerRepository.save(customer);

        String accountNumber = account.getAccountNumber();

        log.info("Account with number {} was created.", accountNumber);

        return new AccountCreatedDTO().accountNumber(accountNumber);
    }

}
