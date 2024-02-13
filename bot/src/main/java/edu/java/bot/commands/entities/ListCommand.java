package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;

public class ListCommand implements Command {
    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        // Логика обработки команды "/list"
        return new SendMessage(update.message().chat().id(), "Список отслеживаемых ссылок:\nссылка1\nссылка2\nссылка3");
    }
}
