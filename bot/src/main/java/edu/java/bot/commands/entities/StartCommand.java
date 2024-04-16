package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.memory.DataManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private static final String START_COMMAND_NAME = "/start";
    private static final String START_COMMAND_DESCRIPTION = "Зарегистрировать пользователя";
    private final DataManager manager;

    @Override
    public String getCommandName() {
        return START_COMMAND_NAME;
    }

    @Override
    public String description() {
        return START_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        return new SendMessage(chatId, manager.registerUser(chatId));
    }

    @Override
    public boolean supportsMessageProcessing(Update update) {
        return update.message().text().startsWith(START_COMMAND_NAME);
    }
}
