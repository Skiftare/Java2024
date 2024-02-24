package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {
    private final ApplicationConfig config;

    @Autowired
    public TelegramBotConfig(ApplicationConfig config) {
        this.config = config;
    }

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(config.telegramToken());
    }

}
