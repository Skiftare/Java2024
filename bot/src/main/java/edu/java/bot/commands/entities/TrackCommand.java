package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.database.DialogManager;
import org.springframework.stereotype.Component;
import java.util.Objects;

import static edu.java.bot.database.WeakLinkChecker.checkLinkWithoutConnecting;
import static edu.java.bot.utility.UtilityStatusClass.SUCCESS_TRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.TRACK_COMMAND_DESCRIPTION;
import static edu.java.bot.utility.UtilityStatusClass.UNSUCCESSFUL_TRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.WAIT_FOR_URL_TRACK_INFO;

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
    public String supports(Update update) {
       String textMessage = update.message().text();
       String result = UNSUCCESSFUL_TRACK_INFO;
       if(Objects.equals(textMessage, this.command())){
           DialogManager.setWaitForTrack(update.message().chat().id());
           result = WAIT_FOR_URL_TRACK_INFO;
           //ErrorLogger.createLog(WAIT_FOR_URL_TRACK_INFO+" from "+update.message().chat().id().toString());
       }
       else{
           String[] parts = textMessage.split(" ");
           if (parts.length > 1) {
               String link = parts[1];
               if(checkLinkWithoutConnecting(link) && DialogManager.trackURL(update)){
                   result = SUCCESS_TRACK_INFO;
               }
           }
       }
       return result;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), supports(update));
    }
}
