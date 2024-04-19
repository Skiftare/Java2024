package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.memory.DataManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private static final String LIST_COMMAND_NAME = "/list";
    private static final String LIST_COMMAND_DESCRIPTION = "Показать список отслеживаемых ссылок";

    private final DataManager manager;

    @Override
    public String getCommandName() {
        return LIST_COMMAND_NAME;
    }

    @Override
    public String description() {
        return LIST_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        var list = manager.getListOFTrackedCommands(chatId);
        if (list.isEmpty()) {
            return new SendMessage(chatId, "Список пуст");
        }
        return new SendMessage(chatId, list);
    }

    @Override
    public boolean supportsMessageProcessing(Update update) {
        return update.message().text().startsWith(LIST_COMMAND_NAME);
    }
}
