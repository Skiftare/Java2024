package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.memory.DialogManager;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    private static final String START_COMMAND_NAME = "/start";
    private static final String START_COMMAND_DESCRIPTION = "Зарегистрировать пользователя";
    private static final String SUCCESS_START_INFO =
        "Бот будет хранить id диалога только если есть хотя бы 1 отслеживаемая ссылка";

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

        DialogManager.resetDialogState(chatId);
        return new SendMessage(chatId, SUCCESS_START_INFO);
    }

    @Override
    public boolean supportsMessageProcessing(Update update) {
        return update.message().text().startsWith(START_COMMAND_NAME);
    }
}
