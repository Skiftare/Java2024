package edu.java.bot.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.entities.HelpCommand;
import edu.java.bot.commands.entities.ListCommand;
import edu.java.bot.commands.entities.StartCommand;
import edu.java.bot.commands.entities.TrackCommand;
import edu.java.bot.commands.entities.UntrackCommand;
import jakarta.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static edu.java.bot.commands.CommandsLoader.getClasses;

@Component
public class BotProcessor {

    @Autowired
    private TelegramBot bot;

    @PostConstruct
    public void setUpdateEventProcessor() {
        bot.setUpdatesListener(this::createUpdatesManager);
    }

    private int createUpdatesManager(List<Update> updates) {
        for (Update update : updates) {
            bot.execute(recognizeCommand(update));
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    private static SendMessage recognizeCommand(Update update) {
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
                            // Активируем метод handle для данного Update
                        Method handleMethod = clazz.getDeclaredMethod("handle", Update.class);
                        msg = (SendMessage) handleMethod.invoke(command, update);
                        foundRightCommand = true;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(!foundRightCommand){
            msg = DialogManager.resolveProblemCommandNotFound(update);
        }


        return msg;
    }
}
