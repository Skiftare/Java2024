package edu.java.bot.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.entities.HelpCommand;
import edu.java.bot.commands.entities.ListCommand;
import edu.java.bot.commands.entities.StartCommand;
import edu.java.bot.commands.entities.TrackCommand;
import edu.java.bot.commands.entities.UntrackCommand;
import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotProcessor {

    @Autowired
    private TelegramBot bot;

    @PostConstruct
    public void setUpdateEventProcessor() {
        bot.setUpdatesListener(this::createUpdatesManager);
    }

    private int createUpdatesManager(List<Update> updates) {
        for (Update update : updates) {
            bot.execute(recognizeCommand(update));
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    private static SendMessage recognizeCommand(Update update) {
        String textInTheCommand = update.message().text();
        SendMessage msg;
        switch (textInTheCommand) {
            //Крч, тут надо CommandsLoader прикрутить, отдать всё на откуп ему. Чтоб он находил нужный класс
            case "/help":
                HelpCommand hc = new HelpCommand();
                msg = hc.handle(update);
                break;
            case "/list":
                msg = new SendMessage(update.message().chat().id(), "Здесь должен быть список.");
                break;
            case "/track":
                DialogManager.markDialogForWaitngToTrack(update);
            case "/untrack":
                DialogManager.markDialogForWaitngToUntrack(update);
            default:
                if (textInTheCommand.startsWith("/track")) {
                    String url = textInTheCommand.replace("/track", "").trim();
                    msg = new SendMessage(update.message().chat().id(), "Вы выбрали трекинг ссылки: " + url);
                } else if (textInTheCommand.startsWith("/untrack")) {
                    String url = textInTheCommand.replace("/untrack", "").trim();

                    msg = new SendMessage(update.message().chat().id(), "Вы выбрали отмену трекинга ссылки: " + url);
                } else {
                    //Тут мы обращаемся к DialogManager-у, чтобы понять, а подходит ли нам эта ссылка для.. трекинга, антрекинга, или INVALID_MESSAGE
                    msg = new SendMessage(update.message().chat().id(), "Неизвестная команда.");
                }
                break;
        }
        return msg;
    }
}
