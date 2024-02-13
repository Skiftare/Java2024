package edu.java.bot.engine;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class MessageManager implements UserMessageProcessor {

    @Override
    public SendMessage process(Update update) {
        long chatId = update.message().chat().id();
        String message = update.message().text();


        return null;





    }
}
