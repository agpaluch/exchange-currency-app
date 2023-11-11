package com.agpaluch.exchange.currency.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Component
@RequiredArgsConstructor
public class RestTemplateFactory {
    private final RestTemplateBuilder restTemplateBuilder;
    private final Logbook logbook;

    public RestTemplate getRestTemplate(IntegrationProperties.Service service) {
        IntegrationProperties.Timeout timeout = service.getTimeout();

        return restTemplateBuilder
                .setConnectTimeout(timeout.getConnect())
                .setReadTimeout(timeout.getResponse())
                .additionalInterceptors(new LogbookClientHttpRequestInterceptor(logbook))
                .build();
    }

}
