package edu.java.database;

import edu.java.data.response.LinkResponse;
import edu.java.data.response.ListLinksResponse;
import edu.java.database.services.interfaces.LinkService;
import edu.java.database.services.interfaces.TgChatService;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.exceptions.entities.UserNotFoundException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DatabaseOperations {
    private final TgChatService tgChatService;
    private final LinkService linkService;
    private final Logger logger = LoggerFactory.getLogger(DatabaseOperations.class);

    public boolean checkExistingOfChat(long chatId) {
        logger.info("Checking if chat with id: " + chatId + " exists");
        return tgChatService.isRegistered(chatId);
    }

    public void registerChat(long chatId) {
        logger.info("Registering chat with id: " + chatId);
        if (tgChatService.isRegistered(chatId)) {
            logger.info("Chat with id: " + chatId + " already exists");
            throw new UserAlreadyExistException("Пользователь " + chatId + " уже существует");
        }
        tgChatService.register(chatId);
    }

    public void deleteChat(Long chatId) {
        checkChatNotFound(chatId, "Нельзя удалить незарегистрированный чат");
        tgChatService.unregister(chatId);
    }

    public List<LinkResponse> getLinks(Long chatId) {
        checkChatNotFound(chatId, "Нельзя получить ссылки для незарегистрированного чата");
        ListLinksResponse result = linkService.listAll(chatId);
        return result.links();
    }

    public LinkResponse addLink(Long chatId, URI link) {
        checkChatNotFound(chatId, "Нельзя добавить ссылку для незарегистрированного чат");
        LinkResponse linkResponse = linkService.add(chatId, new edu.java.data.request.AddLinkRequest(link));
        return linkResponse;
    }

    public LinkResponse removeLink(Long chatId, URI link) {
        checkChatNotFound(chatId, "Нельзя удалить ссылку для незарегистрированного чат");

        linkService.remove(chatId, new edu.java.data.request.RemoveLinkRequest(link));
        throw new LinkNotFoundException("Ссылка отсутствует");
    }

    private void checkChatNotFound(Long chatId, String message) {
        if (!tgChatService.isRegistered(chatId)) {
            throw new UserNotFoundException("Чат не был зарегистрирован");
        }
    }

}
