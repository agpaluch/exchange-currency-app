package com.agpaluch.exchange.currency.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "integration")
public class IntegrationProperties {
    private Nbp nbp;

    public static class Nbp extends Service {

    }

    @Getter
    @Setter
    public abstract static class Service {
        private String baseUrl;
        private Timeout timeout;
    }

    @Getter
    @Setter
    public static class Timeout {
        private Duration connect;
        private Duration response;
    }
}
