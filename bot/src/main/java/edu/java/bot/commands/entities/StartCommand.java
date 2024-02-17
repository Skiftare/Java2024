package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.memory.DialogManager;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.UtilityStatusClass.START_COMMAND_COMMAND;
import static edu.java.bot.utility.UtilityStatusClass.START_COMMAND_DESCRIPTION;
import static edu.java.bot.utility.UtilityStatusClass.SUCCESS_START_INFO;

@Component

public class StartCommand implements Command {
    @Override
    public String command() {
        return START_COMMAND_COMMAND;
    }

    @Override
    public String description() {
        return START_COMMAND_DESCRIPTION;
    }



    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();

        DialogManager.resetDialogState(chatId);
        return new SendMessage(chatId, SUCCESS_START_INFO);
    }
}
