package com.agpaluch.exchange.currency.service;


import com.agpaluch.exchange.currency.client.BidAndAskPriceDto;
import com.agpaluch.exchange.currency.client.ExchangeRateDto;
import com.agpaluch.exchange.currency.client.NbpApiClient;
import com.agpaluch.exchange.currency.entities.Account;
import com.agpaluch.exchange.currency.entities.AccountBalance;
import com.agpaluch.exchange.currency.entities.CurrencyCode;
import com.agpaluch.exchange.currency.exceptions.AccountNotFoundException;
import com.agpaluch.exchange.currency.exceptions.ExchangeRatesNotFoundException;
import com.agpaluch.exchange.currency.exceptions.InvalidBalanceException;
import com.agpaluch.exchange.currency.mapper.CurrencyCodeMapper;
import com.agpaluch.exchange.currency.model.ExchangeCurrencyDTO;
import com.agpaluch.exchange.currency.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeCurrencyService {

    private static final String table = "C";
    private final AccountRepository accountRepository;
    private final CurrencyCodeMapper currencyCodeMapper;
    private final NbpApiClient nbpApiClient;

    @Transactional
    public void exchangeCurrency(String accountNumber, ExchangeCurrencyDTO exchangeCurrencyDTO) throws AccountNotFoundException, InvalidBalanceException, ExchangeRatesNotFoundException {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        CurrencyCode sourceCurrencyCode = currencyCodeMapper.map(exchangeCurrencyDTO.getSourceCurrencyCode());
        CurrencyCode targetCurrencyCode = currencyCodeMapper.map(exchangeCurrencyDTO.getTargetCurrencyCode());

        AccountBalance sourceCurrencyBalance = getSourceCurrencyBalance(account, sourceCurrencyCode, exchangeCurrencyDTO);
        AccountBalance targetCurrencyBalance = getTargetCurrencyBalance(account, targetCurrencyCode, exchangeCurrencyDTO);

        CurrencyCode newTargetCurrencyCode = targetCurrencyCode;
        boolean isBid = false;
        if (CurrencyCode.PLN.equals(targetCurrencyCode)) {
            newTargetCurrencyCode = sourceCurrencyCode;
            isBid = true;
        }
        BidAndAskPriceDto bidAndAskPriceDto = nbpApiClient.getBidAndAskPrice(table, newTargetCurrencyCode.name());

        BigDecimal sourceCurrencyAmount = exchangeCurrencyDTO.getAmountSourceCurrency();
        BigDecimal targetCurrencyAmount = getTargetCurrencyAmount(sourceCurrencyAmount, bidAndAskPriceDto, isBid);

        sourceCurrencyBalance.setBalance(sourceCurrencyBalance.getBalance().subtract(sourceCurrencyAmount));
        targetCurrencyBalance.setBalance(targetCurrencyBalance.getBalance().add(targetCurrencyAmount));

        log.info("Amount of {} {} was exchanged for {} {}.", sourceCurrencyAmount, sourceCurrencyCode, targetCurrencyAmount, targetCurrencyCode);
        log.info("Exchange rate applied was: {}", bidAndAskPriceDto);
    }

    private AccountBalance getSourceCurrencyBalance(Account account, CurrencyCode sourceCurrencyCode, ExchangeCurrencyDTO exchangeCurrencyDTO) throws InvalidBalanceException {
        BigDecimal sourceCurrencyAmount = exchangeCurrencyDTO.getAmountSourceCurrency();

        return account.getAccountBalances().stream()
                .filter(b -> b.getCurrencyCode().equals(sourceCurrencyCode))
                .filter(b -> b.getBalance().compareTo(sourceCurrencyAmount) >= 0)
                .findFirst()
                .orElseThrow(() -> new InvalidBalanceException(String.format("Account balance for currency %s is smaller than %s.", sourceCurrencyCode.name(), sourceCurrencyAmount)));
    }

    private AccountBalance getTargetCurrencyBalance(Account account, CurrencyCode targetCurrencyCode, ExchangeCurrencyDTO exchangeCurrencyDTO) {
        return account.getAccountBalances().stream()
                .filter(b -> b.getCurrencyCode().equals(targetCurrencyCode))
                .findFirst()
                .orElseGet(() -> {
                    AccountBalance targetAccountBalance = AccountBalance.builder().currencyCode(targetCurrencyCode).balance(BigDecimal.ZERO).build();
                    account.addAccountBalance(targetAccountBalance);
                    return targetAccountBalance;
                });
    }

    private BigDecimal getTargetCurrencyAmount(BigDecimal sourceCurrencyAmount, BidAndAskPriceDto bidAndAskPriceDto, boolean isBid) {
        ExchangeRateDto rate = bidAndAskPriceDto.getRates().get(0);
        BigDecimal price;
        if (isBid) {
            price = rate.getBid();
        } else {
            price = BigDecimal.ONE.divide(rate.getAsk(), 4, RoundingMode.HALF_DOWN);
        }

        return sourceCurrencyAmount.multiply(price);
    }

}
