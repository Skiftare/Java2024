package edu.java.api.web;

import edu.java.api.entities.exceptions.BadRequestException;
import edu.java.api.entities.exceptions.NotFoundException;
import edu.java.api.entities.responses.LinkResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class UserClientService {
    private static final String CHAT_IS_ALREADY_REGISTRED = "Chat has been registred already";
    private static final String CANNOT_REPEAT_REGISTER_CHAT = "Cannot repeat register chat";
    private static final String CANNOT_DELETE_UNREGISTERED_CHAT = "Cannot delete unregistered chat";
    private static final String CANNOT_GET_LINKS_UNREGISTERED_CHAT = "Cannot get links for unregistered chat";
    private static final String LINK_ALREADY_TRACKED = "Link is already being tracked";
    private static final String CANNOT_ADD_DUPLICATE_LINK = "Cannot add duplicate link";
    private static final String LINK_NOT_FOUND = "Link not found";
    private static final String CANNOT_REMOVE_MISSING_LINK = "Cannot remove missing link";
    private static final String CHAT_IS_NOT_REGISTERED = "Chat is not registered";
    private final Map<Long, List<LinkResponse>> chatLinks = new HashMap<>();

    public void registerChat(long chatId) {
        if (chatLinks.containsKey(chatId)) {
            throw new BadRequestException(CHAT_IS_ALREADY_REGISTRED, CANNOT_REPEAT_REGISTER_CHAT);
        }

        chatLinks.put(chatId, new ArrayList<>());
    }

    public void deleteChat(Long chatId) {
        checkChatNotFound(chatId, CANNOT_DELETE_UNREGISTERED_CHAT);
        chatLinks.remove(chatId);
    }

    public List<LinkResponse> getLinks(Long chatId) {
        checkChatNotFound(chatId, CANNOT_GET_LINKS_UNREGISTERED_CHAT);
        return chatLinks.get(chatId);
    }

    public LinkResponse addLink(Long chatId, URI link) {
        checkChatNotFound(chatId, CANNOT_ADD_DUPLICATE_LINK);

        List<LinkResponse> linkResponses = chatLinks.get(chatId);
        for (LinkResponse linkResponse : linkResponses) {
            if (linkResponse.url().getPath().equals(link.getPath())) {
                throw new BadRequestException(LINK_ALREADY_TRACKED, CANNOT_ADD_DUPLICATE_LINK);
            }
        }

        LinkResponse linkResponse = new LinkResponse((long) (linkResponses.size() + 1), link);
        linkResponses.add(linkResponse);

        return linkResponse;
    }

    public LinkResponse removeLink(Long chatId, URI link) {
        checkChatNotFound(chatId, CANNOT_REMOVE_MISSING_LINK);

        List<LinkResponse> linkResponses = chatLinks.get(chatId);
        for (LinkResponse linkResponse : linkResponses) {
            if (linkResponse.url().getPath().equals(link.getPath())) {
                linkResponses.remove(linkResponse);
                return linkResponse;
            }
        }

        throw new NotFoundException(LINK_NOT_FOUND, CANNOT_REMOVE_MISSING_LINK);
    }

    private void checkChatNotFound(Long chatId, String message) {
        if (!chatLinks.containsKey(chatId)) {
            throw new NotFoundException(CHAT_IS_NOT_REGISTERED, message);
        }
    }
}
