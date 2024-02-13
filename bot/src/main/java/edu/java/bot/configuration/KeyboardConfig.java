package edu.java.bot.configuration;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyboardConfig {
    public static ReplyKeyboardMarkup createDefaultKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
            new String[]{"Button 1"},
            new String[]{"Button 2"}
        );
        keyboard.resizeKeyboard(true);
        keyboard.oneTimeKeyboard(false);
        keyboard.selective(false);

        return keyboard;
    }
}
