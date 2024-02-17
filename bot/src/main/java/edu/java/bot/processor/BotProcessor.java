package edu.java.bot.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.memory.DialogManager;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

import static edu.java.bot.commands.CommandsLoader.getClasses;
import static edu.java.bot.utility.ErrorLogger.createLog;
import static edu.java.bot.utility.ErrorLogger.createLogError;
import static edu.java.bot.utility.UtilityStatusClass.NAME_OF_HANDLE_METHOD_IN_CLASS_OF_BOT_COMMANDS;

@Component
public class BotProcessor {

    @Autowired
    private TelegramBot bot;

    @PostConstruct
    private void setUpdateEventProcessor() {
        bot.setUpdatesListener(this::createUpdatesManager);
    }

    private int createUpdatesManager(List<Update> updates) {
        for (Update update : updates) {
            createLog("Пришёл update от " + update.message().chat().id().toString() + ", начинаю обработку");
            bot.execute(recognizeCommand(update));
            createLog("Обработал update от " + update.message().chat().id().toString() + ", начинаю обработку");

        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    static SendMessage recognizeCommand(Update update) {
        String textInTheCommand = update.message().text();
        SendMessage msg = null;
        boolean foundRightCommand = false;

        List<Class<?>> classes = getClasses();
        for (Class<?> clazz : classes) {
            try {
                if (Command.class.isAssignableFrom(clazz)) {
                    Command command = (Command) clazz.getDeclaredConstructor().newInstance();
                    String commandOutputs = command.command();
                    if (textInTheCommand.startsWith(commandOutputs)) {
                        createLog("Нашли нужную команду в списке поддерживаемых");
                        Method handleMethod = clazz.getDeclaredMethod(NAME_OF_HANDLE_METHOD_IN_CLASS_OF_BOT_COMMANDS, Update.class);
                        msg = (SendMessage) handleMethod.invoke(command, update);
                        createLog("Ответ создан");
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
            createLog("Ответ создан");
        }
        return msg;
    }
}
