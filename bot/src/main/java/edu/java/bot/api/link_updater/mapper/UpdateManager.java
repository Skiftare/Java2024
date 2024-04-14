package edu.java.bot.api.link_updater.mapper;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.data.request.LinkUpdateRequest;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateManager {
    private final TelegramBot bot;
    private final Logger logger = LoggerFactory.getLogger(UpdateManager.class);
    private final MeterRegistry meterRegistry;
    private Counter userMessagesCounter;

    public boolean addRequest(@NotNull LinkUpdateRequest req) {
        for (long id : req.tgChatIds()) {
            try {
                bot.execute(
                    new SendMessage(
                        id,
                        req.description()
                    )
                );
                userMessagesCounter.increment();
            } catch (Exception e) {
                logger.info("Ошибка при отправке сообщения: {}", e.getMessage());
            }
        }
        return true;
    }

    @PostConstruct
    public void initMetrics() {
        userMessagesCounter = Counter.builder("user_messages")
            .description("Count of processed user messages")
            .register(meterRegistry);
    }
}


