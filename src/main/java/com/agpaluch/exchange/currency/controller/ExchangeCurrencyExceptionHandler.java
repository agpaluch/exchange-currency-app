package com.agpaluch.exchange.currency.controller;


import com.agpaluch.exchange.currency.exception.AccountNotFoundException;
import com.agpaluch.exchange.currency.exception.ExchangeRatesNotFoundException;
import com.agpaluch.exchange.currency.exception.InvalidBalanceException;
import com.agpaluch.exchange.currency.model.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExchangeCurrencyExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleException(AccountNotFoundException exception) {
        String errorMessage = exception.getMessage();
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorDTO(errorMessage));
    }

    @ExceptionHandler({InvalidBalanceException.class, ExchangeRatesNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleException(Exception exception) {
        String errorMessage = exception.getMessage();
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(getErrorDTO(errorMessage));
    }

    private ErrorDTO getErrorDTO(String message) {
        return new ErrorDTO().errorMessage(message);
    }
}
