package edu.java.bot.api.link_updater.mapper;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.data.request.LinkUpdateRequest;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateManager {
    TelegramBot bot;
    private final Logger logger = LoggerFactory.getLogger(UpdateManager.class);

    boolean addRequest(@NotNull LinkUpdateRequest req) {
        for (long id : req.tgChatIds()) {
            try {
                bot.execute(
                    new SendMessage(
                        id,
                        "Ссылка " + req.url().toString() + " обновлена. Обнаружен новый контент:\n" + req.description()
                    )
                );
            } catch (Exception e) {
                logger.info("Ошибка при отправке сообщения: " + e.getMessage());
            }
        }
        return true;
    }
}


