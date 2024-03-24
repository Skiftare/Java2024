package edu.java.database.services.interfaces;

import edu.java.data.request.AddLinkRequest;
import edu.java.data.request.RemoveLinkRequest;
import edu.java.data.response.LinkResponse;
import edu.java.data.response.ListLinksResponse;
import java.net.URI;

public interface LinkService {
    public final static String CHAT_EXCEPTION_TEMPLATE = "Chat with tg chat id =";
    public final static String LINK_EXCEPTION_TEMPLATE = "Link with url =";
    LinkResponse add(long tgChatId, AddLinkRequest linkRequest);

    LinkResponse remove(long tgChatId, RemoveLinkRequest linkRequest);

    ListLinksResponse listAll(long tgChatId);

    default String generateExceptionMessage(String message, String reason) {
        return message + ' ' + reason;
    }
    default String generateExceptionMessageForChatId(long id) {
        return generateExceptionMessage(CHAT_EXCEPTION_TEMPLATE, String.valueOf(id));
    }
    default String generateExceptionMessageForLinkUrl(String url) {
        return generateExceptionMessage(LINK_EXCEPTION_TEMPLATE, url);
    }
}
