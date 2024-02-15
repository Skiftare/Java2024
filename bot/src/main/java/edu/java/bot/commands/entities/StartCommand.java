package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.processor.DialogManager;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.UtilityStatusClass.START_COMMAND_DESCRIPTION;

@Component

public class StartCommand implements Command {
    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return START_COMMAND_DESCRIPTION;
    }



    @Override
    public SendMessage handle(Update update) {
        // Логика обработки команды "/start"
        DialogManager.resetDialogState(update.message().chat().id());
        return new SendMessage(update.message().chat().id(), "Вы зарегистрированы!");
    }
}
