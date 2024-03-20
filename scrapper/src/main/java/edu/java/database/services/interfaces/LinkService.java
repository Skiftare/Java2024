package edu.java.database.services.interfaces;

import edu.java.data.request.AddLinkRequest;
import edu.java.data.request.RemoveLinkRequest;
import edu.java.data.response.LinkResponse;
import edu.java.data.response.ListLinksResponse;

public interface LinkService {
    LinkResponse add(long tgChatId, AddLinkRequest linkRequest);

    LinkResponse remove(long tgChatId, RemoveLinkRequest linkRequest);

    ListLinksResponse listAll(long tgChatId);

    default String generateExceptionMessage(String message, String reason) {
        return message + ' ' + reason;
    }
}
