package edu.java.configuration;

import edu.java.api.web.WebClientForBotCommunication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class WebClientConfiguration {
    @Bean
    public WebClientForBotCommunication botWebClient(ApplicationConfig appConfig) {
        return new WebClientForBotCommunication(
            WebClient.create(appConfig.api().botUrl()),
            appConfig.bot()
        );
    }
}
