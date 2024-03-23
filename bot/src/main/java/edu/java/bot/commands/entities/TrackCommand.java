package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.memory.DialogManager;
import edu.java.bot.processor.BotProcessor;
import edu.java.bot.processor.UserRequest;
import java.util.Objects;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import static edu.java.bot.memory.WeakLinkChecker.checkLinkWithoutConnecting;

@Component

public class TrackCommand implements Command {
    private static final String TRACK_COMMAND_NAME = "/track";
    private static final String TRACK_COMMAND_DESCRIPTION = "Начать отслеживание ссылки";
    private static final String UNSUCCESSFUL_TRACK_INFO = "Ссылка невалидна";
    private static final String SUCCESS_TRACK_INFO = "Отслеживание ссылки начато!";
    private static final String WAIT_FOR_URL_TRACK_INFO = "Жду ссылку на отслеживание";
    private static final String USER_IS_NOT_REGISTERED =
        "Пользователь не зарегистрирован, так что ссыль отслеживаться не будет";
    private final DialogManager manager;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(TrackCommand.class);

    public TrackCommand(DialogManager manager) {
        this.manager = manager;
    }


    @Override
    public String getCommandName() {
        return TRACK_COMMAND_NAME;
    }

    @Override
    public String description() {
        return TRACK_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        String textMessage = update.message().text();
        Long chatId = update.message().chat().id();
        logger.info("Пользователь {} запросил отслеживание {}", chatId, textMessage);
        String result = UNSUCCESSFUL_TRACK_INFO;
        if (Objects.equals(textMessage, this.getCommandName())) {
            if (manager.setWaitForTrack(chatId)) {
                result = WAIT_FOR_URL_TRACK_INFO;
            } else {
                result = USER_IS_NOT_REGISTERED;
            }
        } else {
            String[] parts = textMessage.split(" ");
            if (parts.length > 1) {
                String link = parts[1];
                UserRequest request = new UserRequest(chatId, link);
                if (checkLinkWithoutConnecting(link) && manager.trackURL(request)) {
                    result = SUCCESS_TRACK_INFO;
                }
            }
        }
        return new SendMessage(update.message().chat().id(), result);
    }

    @Override
    public boolean supportsMessageProcessing(Update update) {
        return update.message().text().startsWith(TRACK_COMMAND_NAME);
    }

}

