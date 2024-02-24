package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.memory.DialogManager;
import edu.java.bot.processor.UserRequest;
import org.springframework.stereotype.Component;
import static edu.java.bot.memory.DialogManager.getListOfTracked;
import static edu.java.bot.utility.UtilityStatusClass.LIST_COMMAND_COMMAND;
import static edu.java.bot.utility.UtilityStatusClass.LIST_COMMAND_DESCRIPTION;

@Component
public class ListCommand implements Command {
    @Override
    public String getCommandName() {
        return LIST_COMMAND_COMMAND;
    }

    @Override
    public String description() {
        return LIST_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();

        DialogManager.resetDialogState(chatId);
        return new SendMessage(chatId, getListOfTracked(new UserRequest(chatId, update.message().text()))
        );
    }
    @Override
    public boolean supportsMessageProcessing(Update update){
        return update.message().text().startsWith(LIST_COMMAND_COMMAND);
    }
}
