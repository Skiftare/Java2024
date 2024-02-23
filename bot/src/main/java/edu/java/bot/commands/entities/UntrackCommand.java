package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.memory.DialogManager;
import edu.java.bot.processor.UserRequest;
import java.util.Objects;
import org.springframework.stereotype.Component;
import static edu.java.bot.memory.WeakLinkChecker.checkLinkWithoutConnecting;
import static edu.java.bot.utility.UtilityStatusClass.SPACE_AS_SPLIT_CHAR;
import static edu.java.bot.utility.UtilityStatusClass.SUCCESS_UNTRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.UNSUCCESSFUL_UNTRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.UNTRACK_COMMAND_COMMAND;
import static edu.java.bot.utility.UtilityStatusClass.UNTRACK_COMMAND_DESCRIPTION;
import static edu.java.bot.utility.UtilityStatusClass.WAIT_FOR_URL_UNTRACK_INFO;

@Component
public class UntrackCommand implements Command {
    @Override
    public String getCommandName() {
        return UNTRACK_COMMAND_COMMAND;
    }

    @Override
    public String description() {
        return UNTRACK_COMMAND_DESCRIPTION;
    }

    public String supports(Update update) {
        String textMessage = update.message().text();
        Long chatId = update.message().chat().id();
        String result = UNSUCCESSFUL_UNTRACK_INFO;
        if (Objects.equals(textMessage, this.getCommandName())) {
            DialogManager.setWaitForUntrack(chatId);
            result = WAIT_FOR_URL_UNTRACK_INFO;
        } else {
            String[] parts = textMessage.split(SPACE_AS_SPLIT_CHAR);
            if (parts.length > 1) {
                String link = parts[1];
                UserRequest request = new UserRequest(chatId, link);
                if (checkLinkWithoutConnecting(link) && DialogManager.untrackURL(request)) {
                    result = SUCCESS_UNTRACK_INFO;
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

