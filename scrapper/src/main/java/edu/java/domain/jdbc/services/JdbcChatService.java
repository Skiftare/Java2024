package edu.java.domain.jdbc.services;

import edu.java.database.services.interfaces.TgChatService;
import edu.java.domain.jdbc.dao.JdbcChatDao;
import edu.java.domain.jdbc.dao.JdbcLinkChatRelationDao;
import edu.java.domain.jdbc.dao.JdbcLinkDao;
import edu.java.domain.jdbc.written.chat.Chat;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkRelation;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.exceptions.entities.UserNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JdbcChatService implements TgChatService {
    private final JdbcChatDao chatDao;
    private final JdbcLinkDao linkDao;
    private final JdbcLinkChatRelationDao chatLinkDao;
    private final Logger logger = LoggerFactory.getLogger(JdbcChatService.class);

    @Override
    public void register(long tgChatId) {
        if (chatDao.getByTgChatId(tgChatId).isPresent()) {
            throw new UserAlreadyExistException(generateExceptionMessageForChatId(tgChatId));
        }
        Chat chat = Chat.makeChat(tgChatId, OffsetDateTime.now());
        chatDao.save(chat);
    }

    @Override
    public void unregister(long tgChatId) {
        long id = chatDao.getByTgChatId(tgChatId)
            .orElseThrow(
                () -> new UserNotFoundException(generateExceptionMessageForChatId(tgChatId))
            ).getTgChatId();
        List<ChatLinkRelation> links = chatLinkDao.getByChatId(id);
        for (ChatLinkRelation chatLink : links) {
            long linkId = chatLink.getDataLinkId();
            int countChatTrackLink = chatLinkDao.getByLinkId(linkId).size();
            chatLinkDao.delete(id, linkId);
            if (countChatTrackLink == 1) {
                linkDao.deleteByDataLinkId(linkId);
            }
        }
        chatDao.deleteByTgChatId(tgChatId);
    }

    @Override
    public boolean isRegistered(long tgChatId) {
        logger.info("Checking if chat with id: " + tgChatId + " exists with JDBC");
        return chatDao.getByTgChatId(tgChatId).isPresent();
    }
}
