package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.CommandsLoader;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotConfig {
    private static final String MESSAGE_TO_ADMIN_OF_STARTUP = "Bot has been activated";

    @Autowired
    private ApplicationConfig config;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(config.telegramToken());
        bot.execute(new SendMessage(
            config.adminChatId(), MESSAGE_TO_ADMIN_OF_STARTUP).replyMarkup(defaultKeyboard())
        );
        return bot;
    }

    private ReplyKeyboardMarkup defaultKeyboard() {

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
