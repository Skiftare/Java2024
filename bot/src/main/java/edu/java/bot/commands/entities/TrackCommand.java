package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.processor.DialogManager;
import org.springframework.stereotype.Component;
import java.net.URI;
import static edu.java.bot.utility.UtilityStatusClass.SUCCESS_TRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.TRACK_COMMAND_DESCRIPTION;
import static edu.java.bot.utility.UtilityStatusClass.UNSUCCESSFUL_TRACK_INFO;

@Component

public class TrackCommand implements Command {
    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return TRACK_COMMAND_DESCRIPTION;
    }
    public boolean supports(Update update) {
        return DialogManager.isValidUrlForTracking(URI.create(update.message().text()));
    }

    @Override
    public SendMessage handle(Update update) {
        // Логика обработки команды "/track"
        if(supports(update)) {
            return new SendMessage(update.message().chat().id(), SUCCESS_TRACK_INFO);
        }
        else{
            return new SendMessage(update.message().chat().id(), UNSUCCESSFUL_TRACK_INFO);
        }

    }
}
