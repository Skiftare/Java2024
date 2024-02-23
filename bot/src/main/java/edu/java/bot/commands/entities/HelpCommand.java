package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandsLoader;
import edu.java.bot.memory.DialogManager;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static edu.java.bot.utility.UtilityStatusClass.HELP_COMMAND_COMMAND;
import static edu.java.bot.utility.UtilityStatusClass.HELP_COMMAND_DESCRIPTION;

@Component
public class HelpCommand implements Command {
    private CommandsLoader loader;
    private static String HELP_INFO = "mock message";

    @Autowired
    public HelpCommand(CommandsLoader loader) {
        this.loader = loader;
    }

    @Override
    public String getCommandName() {
        return HELP_COMMAND_COMMAND;
    }

    @Override
    public String description() {
        return HELP_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        System.out.println(CommandsLoader.getCommandsWithDescription());
        if (Objects.equals(HELP_INFO, "mock_message")){
            HELP_INFO = CommandsLoader.getCommandsWithDescription();
            System.out.println("downloaded");
        }
        DialogManager.resetDialogState(chatId);
        return new SendMessage(chatId, HELP_INFO);
    }
}
