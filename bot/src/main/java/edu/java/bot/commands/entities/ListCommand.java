package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.UtilityStatusClass.LIST_COMMAND_DESCRIPTION;

@Component
public class ListCommand implements Command {
    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return LIST_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), "Список отслеживаемых ссылок:\nссылка1\nссылка2\nссылка3");
    }
}
