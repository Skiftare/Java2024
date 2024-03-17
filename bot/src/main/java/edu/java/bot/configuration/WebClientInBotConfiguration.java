package edu.java.bot.configuration;

import edu.java.bot.api.web.WebClientForScrapperCommunication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientInBotConfiguration {

    @Bean
    public WebClientForScrapperCommunication scrapperWebClient(ApplicationConfig appConfig) {
        return new WebClientForScrapperCommunication(appConfig.api().scrapperUrl());
    }
}
