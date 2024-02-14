package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.processor.DialogManager;
import org.springframework.stereotype.Component;

import java.net.URI;
import static edu.java.bot.utility.UtilityStatusClass.SUCCESS_UNTRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.UNSUCCESSFUL_UNTRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.UNTRACK_COMMAND_DESCRIPTION;

@Component
public class UntrackCommand implements Command {
    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return UNTRACK_COMMAND_DESCRIPTION;
    }

    public boolean supports(Update update) {
        return DialogManager.isValidUrlForUntracking(URI.create(update.message().text()));
    }


    @Override
    public SendMessage handle(Update update) {
        if(supports(update)) {
            return new SendMessage(update.message().chat().id(), SUCCESS_UNTRACK_INFO);
        }
        else{
            return new SendMessage(update.message().chat().id(), UNSUCCESSFUL_UNTRACK_INFO);
        }
    }
}

