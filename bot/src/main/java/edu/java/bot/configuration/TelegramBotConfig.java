package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

    @Autowired
    private ApplicationConfig config;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(config.telegramToken());
        return bot;
    }

}
