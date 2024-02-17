package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.memory.DialogManager;
import edu.java.bot.processor.UserRequest;
import edu.java.bot.utility.ErrorLogger;
import org.springframework.stereotype.Component;
import java.util.Objects;

import static edu.java.bot.memory.WeakLinkChecker.checkLinkWithoutConnecting;
import static edu.java.bot.utility.UtilityStatusClass.SPACE_AS_SPLIT_CHAR;
import static edu.java.bot.utility.UtilityStatusClass.SUCCESS_TRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.TRACK_COMMAND_COMMAND;
import static edu.java.bot.utility.UtilityStatusClass.TRACK_COMMAND_DESCRIPTION;
import static edu.java.bot.utility.UtilityStatusClass.UNSUCCESSFUL_TRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.WAIT_FOR_URL_TRACK_INFO;

@Component

public class TrackCommand implements Command {
    @Override
    public String command() {
        return TRACK_COMMAND_COMMAND;
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
       }
       else{
           String[] parts = textMessage.split(SPACE_AS_SPLIT_CHAR);
           if (parts.length > 1) {

               if(checkLinkWithoutConnecting(parts[1]) && DialogManager.trackURL(
                   new UserRequest(update.message().chat().id(),parts[1])
               )){
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
