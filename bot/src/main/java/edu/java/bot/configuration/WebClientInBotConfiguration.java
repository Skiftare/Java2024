package edu.java.bot.configuration;

import edu.java.bot.api.web.WebClientForScrapperCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientInBotConfiguration {

    @Bean
    public WebClientForScrapperCommunication scrapperWebClient(ApplicationConfig appConfig) {
        Logger logger = LoggerFactory.getLogger(WebClientInBotConfiguration.class);
        logger.info("Scrapper URL: " + appConfig.api().scrapperUrl());
        return new WebClientForScrapperCommunication(appConfig.api().scrapperUrl());
    }
}
