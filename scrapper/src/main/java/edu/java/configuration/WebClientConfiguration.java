package edu.java.configuration;

import edu.java.api.web.WebClientForBotCommunication;
import io.github.resilience4j.retry.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    private final Retry retry;

    public WebClientConfiguration(
        @Autowired
        @Qualifier("botRetry")
        Retry retry
    ) {
        this.retry = retry;
    }

    @Bean
    public WebClientForBotCommunication botWebClient(ApplicationConfig appConfig) {
        return new WebClientForBotCommunication(
            WebClient.create(appConfig.api().botUrl()),
            retry
        );
    }
}
