package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.memory.DialogManager;
import edu.java.bot.processor.UserRequest;
import java.util.Objects;
import org.springframework.stereotype.Component;
import static edu.java.bot.memory.WeakLinkChecker.checkLinkWithoutConnecting;

@Component
public class UntrackCommand implements Command {
    private static final String UNTRACK_COMMAND_NAME = "/untrack";
    private static final String UNTRACK_COMMAND_DESCRIPTION = "Прекратить отслеживание ссылки";
    private static final String SUCCESS_UNTRACK_INFO = "Отслеживание ссылки прекращено!";
    private static final String UNSUCCESSFUL_UNTRACK_INFO = "Ссылка невалидна или отсутсвует в отслеживаемых";
    private static final String WAIT_FOR_URL_UNTRACK_INFO = "Жду ссылку на удаление";
    private final DialogManager manager;

    public UntrackCommand(DialogManager manager) {
        this.manager = manager;
    }


    @Override
    public String getCommandName() {
        return UNTRACK_COMMAND_NAME;
    }

    @Override
    public String description() {
        return UNTRACK_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        String textMessage = update.message().text();
        Long chatId = update.message().chat().id();
        String result = UNSUCCESSFUL_UNTRACK_INFO;
        if (Objects.equals(textMessage, this.getCommandName())) {
            manager.setWaitForUntrack(chatId);
            result = WAIT_FOR_URL_UNTRACK_INFO;
        } else {
            String[] parts = textMessage.split(" ");
            if (parts.length > 1) {
                String link = parts[1];
                UserRequest request = new UserRequest(chatId, link);
                if (checkLinkWithoutConnecting(link) && manager.untrackURL(request)) {
                    result = SUCCESS_UNTRACK_INFO;
                }
            }
        }
        return new SendMessage(update.message().chat().id(), result);
    }

    @Override
    public boolean supportsMessageProcessing(Update update) {
        return update.message().text().startsWith(UNTRACK_COMMAND_NAME);
    }
}

