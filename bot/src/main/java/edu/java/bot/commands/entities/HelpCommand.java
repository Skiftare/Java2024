package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandsLoader;
import edu.java.bot.memory.DialogManager;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.UtilityStatusClass.HELP_COMMAND_COMMAND;
import static edu.java.bot.utility.UtilityStatusClass.HELP_COMMAND_DESCRIPTION;

@Component
public class HelpCommand implements Command {
    private static final String HELP_INFO = CommandsLoader.getCommandsWithDescription();


    @Override
    public String command() {
        return HELP_COMMAND_COMMAND;
    }

    @Override
    public String description() {
        return HELP_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();

        DialogManager.resetDialogState(chatId);
        return new SendMessage(chatId, HELP_INFO);
    }
}
