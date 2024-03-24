package edu.java.bot.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.entities.Command;
import edu.java.bot.commands.loaders.CommandsLoader;
import edu.java.bot.memory.Cookie;
import edu.java.bot.memory.CookieState;
import edu.java.bot.memory.DialogManager;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotProcessor {

    private final TelegramBot bot;
    private final ReplyKeyboardMarkup replyKeyboardMarkup;
    private final CommandsLoader loader;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(BotProcessor.class);

    @Autowired BotProcessor(
        TelegramBot bot,
        CommandsLoader loader,
        ReplyKeyboardMarkup keyboard
    ) {
        this.bot = bot;
        this.bot.setUpdatesListener(this::createUpdatesManager);
        this.loader = loader;
        this.replyKeyboardMarkup = keyboard;
    }

    private int createUpdatesManager(List<Update> updates) {
        for (Update update : updates) {
            bot.execute(recognizeCommand(update).replyMarkup(replyKeyboardMarkup));
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    SendMessage recognizeCommand(Update update) {
        SendMessage msg = new SendMessage(update.message().chat().id(), "Неизвестная команда");
        List<Command> availableCommand = loader.getCommandsList();
        for (Command command : availableCommand) {
            if (command.supportsMessageProcessing(update)) {
                try {
                    msg = command.handle(update);
                    break;
                }
                catch (Exception e) {
                    logger.error("Error while handling command", e);
                }
            }
        }
        return msg;
    }
}
