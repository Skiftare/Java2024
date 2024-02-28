package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.loaders.CommandLoaderForHelpMessage;
import edu.java.bot.memory.DialogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    private static final String HELP_COMMAND_NAME = "/help";
    private static final String HELP_COMMAND_DESCRIPTION = "Вывести окно с командами";

    private final CommandLoaderForHelpMessage loader;

    @Autowired
    public HelpCommand(CommandLoaderForHelpMessage loader) {
        this.loader = loader;
    }

    @Override
    public String getCommandName() {
        return HELP_COMMAND_NAME;
    }

    @Override
    public String description() {
        return HELP_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        DialogManager.resetDialogState(chatId);
        return new SendMessage(chatId, loader.getCommandsWithDescription());
    }

    @Override
    public boolean supportsMessageProcessing(Update update) {
        return update.message().text().startsWith(HELP_COMMAND_NAME);
    }
}
