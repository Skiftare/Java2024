package edu.java.bot.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandsLoader;
import edu.java.bot.memory.DialogManager;
import jakarta.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import static edu.java.bot.utility.ErrorLogger.createLog;
import static edu.java.bot.utility.ErrorLogger.createLogError;
import static edu.java.bot.utility.UtilityStatusClass.NAME_OF_HANDLE_METHOD_IN_CLASS_OF_BOT_COMMANDS;

@Service
public class BotProcessor {

    private TelegramBot bot;
    private ReplyKeyboardMarkup replyKeyboardMarkup;
    private CommandsLoader loader;

    @Autowired
    BotProcessor(TelegramBot bot){
        this.bot = bot;
        this.bot.setUpdatesListener(this::createUpdatesManager);




    }



    private int createUpdatesManager(List<Update> updates) {
        for (Update update : updates) {
            bot.execute(recognizeCommand(update).replyMarkup(replyKeyboardMarkup));
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    SendMessage recognizeCommand(Update update) {
        List<String> listOfCommands = CommandsLoader.getCommandsList();

        KeyboardButton[] buttons = new KeyboardButton[listOfCommands.size()];

        for (int i = 0; i < listOfCommands.size(); i++) {
            buttons[i] = new KeyboardButton(listOfCommands.get(i));
        }

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(buttons);
        keyboard.resizeKeyboard(true);
        keyboard.oneTimeKeyboard(false);
        keyboard.selective(false);
        this.replyKeyboardMarkup = keyboard;


        String textInTheCommand = update.message().text();
        SendMessage msg = null;
        boolean foundRightCommand = false;

        List<Class<?>> classes = CommandsLoader.getClasses();
        for (Class<?> clazz : classes) {
            try {
                if (Command.class.isAssignableFrom(clazz)) {
                    Command command = (Command) clazz.getDeclaredConstructor().newInstance();
                    String commandOutputs = command.getCommandName();
                    if (textInTheCommand.startsWith(commandOutputs)) {
                        createLog("Нашли нужную команду в списке поддерживаемых");
                        Method handleMethod = clazz.getDeclaredMethod(
                                NAME_OF_HANDLE_METHOD_IN_CLASS_OF_BOT_COMMANDS, Update.class
                        );
                        msg = (SendMessage) handleMethod.invoke(command, update);
                        foundRightCommand = true;
                        break;
                    }

                }
            } catch (Exception e) {
                createLogError(e.getMessage());
            }
        }

        if (!foundRightCommand) {
            createLog("Нужной команды нет, DialogManager начинает работу");
            msg = DialogManager.resolveProblemCommandNotFound(
                    new UserRequest(update.message().chat().id(), update.message().text())
            );
        }
        return msg;
    }
}
