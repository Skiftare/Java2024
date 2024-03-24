package edu.java.configuration;

import edu.java.api.WebClientForBotCommunication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Bean
    public WebClientForBotCommunication botWebClient(ApplicationConfig appConfig) {
        return new WebClientForBotCommunication(WebClient.create(appConfig.api().botUrl()));
    }
}
