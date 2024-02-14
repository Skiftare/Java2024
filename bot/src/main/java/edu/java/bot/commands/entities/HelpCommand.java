package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandsLoader;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.UtilityStatusClass.HELP_COMMAND_DESCRIPTION;

@Component
public class HelpCommand implements Command {
    private static final String helpInfo = CommandsLoader.getCommandsWithDescription();
    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return HELP_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), helpInfo);
    }
}
