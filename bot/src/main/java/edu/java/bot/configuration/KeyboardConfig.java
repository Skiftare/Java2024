package edu.java.bot.configuration;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import edu.java.bot.commands.CommandsLoader;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyboardConfig {
    @Bean
    public ReplyKeyboardMarkup createKeyboard() {
        List<String> listOfCommands = CommandsLoader.getCommandsList();
        KeyboardButton[] buttons = new KeyboardButton[listOfCommands.size()];

        for (int i = 0; i < listOfCommands.size(); i++) {
            buttons[i] = new KeyboardButton(listOfCommands.get(i));
        }

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(buttons);
        keyboard.resizeKeyboard(true);
        keyboard.oneTimeKeyboard(false);
        keyboard.selective(false);
        return keyboard;
    }
}
