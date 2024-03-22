package edu.java.bot.api.link_updater.mapper;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.data.request.LinkUpdate;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UpdateManager {
    TelegramBot bot;

    boolean addRequest(@NotNull LinkUpdate req) {
        for (long id : req.tgChatIds()) {
            try {
                bot.execute(
                    new SendMessage(
                        id,
                        "Ссылка " + req.url().toString() + " обновлена. Обнаружен новый контент:\n" + req.description()
                    )
                );
            } catch (Exception e) {

            }
        }
        return true;
    }
}


