package com.agpaluch.exchange.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ExchangeCurrencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeCurrencyApplication.class, args);
    }

}
