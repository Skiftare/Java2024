package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;

public class UntrackCommand implements Command {
    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Прекратить отслеживание ссылки";
    }

    public boolean supports(Update update) {
        return
    }

    @Override
    public SendMessage handle(Update update) {
        // Логика обработки команды "/untrack"
        if(supports(update)) {
            return new SendMessage(update.message().chat().id(), "Отслеживание ссылки прекращено!");
        }
        else{
            return new SendMessage(update.message().chat().id(), "Отслеживание ссылки ghtrhfnbnm yt gjkexbkjcm");
        }
    }
}

