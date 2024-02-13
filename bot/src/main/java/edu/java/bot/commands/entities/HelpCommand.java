package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;

public class HelpCommand implements Command {
    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Вывести окно с командами";
    }



    @Override
    public SendMessage handle(Update update) {
        // Логика обработки команды "/help"
        return new SendMessage(update.message().chat().id(), "qw");
    }
}
