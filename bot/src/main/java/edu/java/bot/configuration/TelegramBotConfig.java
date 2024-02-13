package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class TelegramBotConfig {

    @Value("${app.telegram-token}")
    private String telegramToken;

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(telegramToken);
    }

    @Bean
    public ReplyKeyboardMarkup defaultKeyboard() {
        return KeyboardConfig.createDefaultKeyboard();
    }

}
