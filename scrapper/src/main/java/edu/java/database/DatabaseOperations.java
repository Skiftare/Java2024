package edu.java.database;

import edu.java.data.response.LinkResponse;
import edu.java.exceptions.entities.LinkAlreadyExistException;
import edu.java.exceptions.entities.LinkNotFoundException;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.exceptions.entities.UserNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseOperations {
    private final Map<Long, List<LinkResponse>> chatLinks = new HashMap<>();

    public boolean checkExistingOfChat(long chatId) {
        return chatLinks.containsKey(chatId);
    }

    public void registerChat(long chatId) {
        if (chatLinks.containsKey(chatId)) {
            throw new UserAlreadyExistException("Пользователь " + chatId + " уже существует");
        }

        chatLinks.put(chatId, new ArrayList<>());
    }

    public void deleteChat(Long chatId) {
        checkChatNotFound(chatId, "Нельзя удалить незарегистрированный чат");
        chatLinks.remove(chatId);
    }

    public List<LinkResponse> getLinks(Long chatId) {
        checkChatNotFound(chatId, "Нельзя получить ссылки для незарегистрированного чата");
        return chatLinks.get(chatId);
    }

    public LinkResponse addLink(Long chatId, URI link) {
        checkChatNotFound(chatId, "Нельзя добавить ссылку для незарегистрированного чат");

        List<LinkResponse> linkResponses = chatLinks.get(chatId);
        for (LinkResponse linkResponse : linkResponses) {
            if (linkResponse.url().equals(link.getPath())) {
                throw new LinkAlreadyExistException("Ссылка уже отслеживается");
            }
        }

        LinkResponse linkResponse = new LinkResponse((long) (linkResponses.size() + 1), link.toString());
        linkResponses.add(linkResponse);

        return linkResponse;
    }

    public LinkResponse removeLink(Long chatId, URI link) {
        checkChatNotFound(chatId, "Нельзя удалить ссылку для незарегистрированного чат");

        List<LinkResponse> linkResponses = chatLinks.get(chatId);
        for (LinkResponse linkResponse : linkResponses) {
            if (linkResponse.url().equals(link.getPath())) {
                linkResponses.remove(linkResponse);
                return linkResponse;
            }
        }
        throw new LinkNotFoundException("Ссылка отсутствует");
    }

    private void checkChatNotFound(Long chatId, String message) {
        if (!chatLinks.containsKey(chatId)) {
            throw new UserNotFoundException("Чат не был зарегистрирован");
        }
    }

}
