package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;

public class StartCommand implements Command {
    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Зарегистрировать пользователя";
    }



    @Override
    public SendMessage handle(Update update) {
        // Логика обработки команды "/start"
        return new SendMessage(update.message().chat().id(), "Вы зарегистрированы!");
    }
}
