package com.agpaluch.exchange.currency.client;

import com.agpaluch.exchange.currency.config.IntegrationProperties;
import com.agpaluch.exchange.currency.config.RestTemplateFactory;
import com.agpaluch.exchange.currency.exception.ExchangeRatesNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NbpApiClient {

    private final RestTemplateFactory restTemplateFactory;
    private final IntegrationProperties integrationProperties;

    public BidAndAskPriceDto getBidAndAskPrice(String table, String code) throws ExchangeRatesNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<BidAndAskPriceDto> response = getRestTemplate()
                .exchange(integrationProperties.getNbp().getBaseUrl() + "/{table}/{code}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        BidAndAskPriceDto.class,
                        table,
                        code);

        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new ExchangeRatesNotFoundException(code);
        }

        return response.getBody();
    }

    private RestTemplate getRestTemplate() {
        return restTemplateFactory.getRestTemplate(integrationProperties.getNbp());
    }

}
