package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.processor.DialogManager;
import org.springframework.stereotype.Component;

import java.net.URI;
import static edu.java.bot.database.WeakLinkChecker.checkLinkWithoutConnecting;
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
        String textMessage = update.message().text();
        boolean result = false;
        if(textMessage == this.command()){
            DialogManager.setWaitForUntrack(update.message().chat().id());
            result = true;
        }
        else{
            String[] parts = textMessage.split(" ");
            if (parts.length > 1) {
                String link = parts[1];
                if(checkLinkWithoutConnecting(link)){
                    result = DialogManager.untrackURL(link);
                }
            }
        }
        return result;
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

