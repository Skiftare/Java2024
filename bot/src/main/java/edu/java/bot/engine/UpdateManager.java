package edu.java.bot.engine;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UpdateManager implements UpdatesListener {

    @Autowired
    private final TelegramBot bot;

    @Autowired
    private MessageManager dialog;

    public UpdateManager(TelegramBot bot) {
        this.bot = bot;
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            Long chatId = update.message().chat().id();
            //Тут проверка ссылок
            bot.execute(dialog.process(update));

        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
