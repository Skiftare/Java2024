package edu.java.domain.jdbc.services;

import edu.java.data.request.AddLinkRequest;
import edu.java.data.request.RemoveLinkRequest;
import edu.java.data.response.LinkResponse;
import edu.java.data.response.ListLinksResponse;
import edu.java.database.services.interfaces.LinkService;
import edu.java.domain.dto_chat_links.ChatLinkWithUrl;
import edu.java.domain.jdbc.dao.JdbcChatDao;
import edu.java.domain.jdbc.dao.JdbcLinkChatRelationDao;
import edu.java.domain.jdbc.dao.JdbcLinkDao;
import edu.java.domain.jdbc.written.chat.Chat;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkRelation;
import edu.java.domain.jdbc.written.link.Link;
import edu.java.exceptions.entities.LinkAlreadyExistException;
import edu.java.exceptions.entities.LinkNotFoundException;
import edu.java.exceptions.entities.UserNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.exceptions.ExceptionMessagesUtilityClass.EXCEPTION_CHAT_MESSAGE_TEMPLATE;
import static edu.java.exceptions.ExceptionMessagesUtilityClass.EXCEPTION_LINK_MESSAGE_TEMPLATE;

@Service
@RequiredArgsConstructor
@Transactional
public class JdbcLinkService implements LinkService {
    private final JdbcChatDao chatDao;
    private final JdbcLinkDao linkDao;
    private final JdbcLinkChatRelationDao chatLinkDao;

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        long chatId = getChatByTgChatId(tgChatId).getTgChatId();

        List<ChatLinkWithUrl> chatLinksByChat = chatLinkDao.getByChatIdJoinLink(chatId);
        List<LinkResponse> linkResponses = chatLinksByChat.stream()
            .map(row -> new LinkResponse(row.getLinkId(), row.getUrl()))
            .toList();

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    public LinkResponse add(long tgChatId, AddLinkRequest linkRequest) {
        String url = linkRequest.link().toString();
        long chatId = getChatByTgChatId(tgChatId).getTgChatId();
        Link actualLink;

        //Создание ссылки в таблице ссылок, если ее нет
        if (linkDao.findByUrl(url).isEmpty()) {
            Link createLink = Link.makeLink(url, OffsetDateTime.now(), OffsetDateTime.now());
            linkDao.save(createLink);
            actualLink = linkDao.findByUrl(url).get();
        } else {
            //Иначе проверка на предмет повторного добавления
            actualLink = linkDao.findByUrl(url).get();
            for (ChatLinkRelation chatLink : chatLinkDao.getByChatId(chatId)) {
                if (chatLink.getDataLinkId() == actualLink.getDataLinkId()) {
                    throw new LinkAlreadyExistException(
                        generateExceptionMessage(EXCEPTION_CHAT_MESSAGE_TEMPLATE, String.valueOf(tgChatId))
                            + ", "
                            + generateExceptionMessage(EXCEPTION_LINK_MESSAGE_TEMPLATE, actualLink.getUrl())
                    );
                }
            }
        }

        ChatLinkRelation chatLink = ChatLinkRelation.makeChatLinkRelation(chatId, actualLink.getDataLinkId());
        chatLinkDao.save(chatLink);

        return new LinkResponse(actualLink.getDataLinkId(), actualLink.getUrl());
    }

    @Override
    public LinkResponse remove(long tgChatId, RemoveLinkRequest linkRequest) {
        String url = linkRequest.link().toString();
        Link actualLink = getLinkByUrl(url);
        long chatId = getChatByTgChatId(tgChatId).getTgChatId();
        long linkId = actualLink.getDataLinkId();
        int countChatTrackLink = chatLinkDao.getByLinkId(linkId).size();
        chatLinkDao.delete(chatId, linkId);
        //Data optimizing
        if (countChatTrackLink == 1) {
            linkDao.deleteByLink(url);
        }

        return new LinkResponse(actualLink.getDataLinkId(), actualLink.getUrl());
    }

    private Chat getChatByTgChatId(long id) {
        return chatDao.getByTgChatId(id)
            .orElseThrow(
                () -> new UserNotFoundException(generateExceptionMessage(
                    EXCEPTION_CHAT_MESSAGE_TEMPLATE,
                    String.valueOf(id)
                ))
            );
    }

    private Link getLinkByUrl(String url) {
        return linkDao.findByUrl(url)
            .orElseThrow(
                () -> new LinkNotFoundException(
                    generateExceptionMessage(EXCEPTION_LINK_MESSAGE_TEMPLATE, url)
                )
            );
    }
}
