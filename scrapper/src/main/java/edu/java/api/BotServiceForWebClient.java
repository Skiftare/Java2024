package edu.java.api;

import edu.java.api.web.UpdateClient;
import edu.java.data.request.LinkUpdateRequest;
import edu.java.exceptions.entities.UserNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotServiceForWebClient {
    private final UpdateClient botClient;
    private final UserClientService chatService;
    Logger logger = Logger.getLogger(BotServiceForWebClient.class.getName());

    public void sendUpdate(long id, String url, String description, List<Long> tgChatIds) {
        try {
            logger.info("Sending update to bot");
            logger.info("Amount of intrested users: " + tgChatIds.size());
            botClient.sendUpdate(new LinkUpdateRequest(URI.create(url), description, tgChatIds));
        } catch (UserNotFoundException ex) {
            deleteNonExtentIds(ex.getMessage());
        } catch (IllegalArgumentException ignore) {
            logger.info("IllegalArgumentException");
        }
    }

    private void deleteNonExtentIds(String message) {
        String[] parts = message.split("\\s+");
        List<Long> badIds = new ArrayList<>();

        for (int i = 2; i < parts.length; i++) {
            try {
                long number = Long.parseLong(parts[i]);
                logger.info("This id does not answer :(" + number);
                badIds.add(number);
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
