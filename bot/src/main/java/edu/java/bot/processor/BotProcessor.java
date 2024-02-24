package edu.java.bot.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.entities.Command;
import edu.java.bot.commands.loaders.CommandsLoader;
import java.util.List;
import edu.java.bot.configuration.KeyboardConfig;
import edu.java.bot.memory.DialogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static edu.java.bot.utility.UtilityStatusClass.UNKNOWN_COMMAND_INFO;

@Service
public class BotProcessor {

    private TelegramBot bot;
    private ReplyKeyboardMarkup replyKeyboardMarkup;
    private CommandsLoader loader;

    @Autowired
    BotProcessor(TelegramBot bot, CommandsLoader loader, ReplyKeyboardMarkup keyboard){
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



        String textInTheCommand = update.message().text();
        SendMessage msg = null;
        boolean foundRightCommand = false;
        List<Command> availableCommand = loader.getCommandsList();
        for(Command command: availableCommand){
            if(command.supportsMessageProcessing(update)){
                msg = command.handle(update);
                foundRightCommand = true;
                break;
            }
        }
        if(!foundRightCommand){
            msg = DialogManager.resolveCommandNeedCookie(new UserRequest(update.message().chat().id(),
                update.message().text()));
        }

        return msg;
    }
}
