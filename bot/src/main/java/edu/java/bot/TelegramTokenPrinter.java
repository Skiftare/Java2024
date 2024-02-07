package edu.java.bot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramTokenPrinter {

    @Value("${app.telegram-token}")
    private String telegramToken;

    public void printToken() {
        System.out.println("Telegram Token: " + telegramToken);
    }

}
