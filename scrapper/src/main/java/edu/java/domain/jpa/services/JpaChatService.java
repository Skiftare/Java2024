package edu.java.domain.jpa.services;

import edu.java.database.services.interfaces.TgChatService;
import edu.java.domain.jpa.dao.Chat;
import edu.java.domain.jpa.written.JpaChatRepository;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.exceptions.entities.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
@Transactional
@EnableTransactionManagement
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaChatService implements TgChatService {

    private final JpaChatRepository chatRepository;

    @Override
    public void register(long tgChatId) {
        if (chatRepository.findChatByTgChatId(tgChatId).isPresent()) {
            throw new UserAlreadyExistException(generateExceptionMessageForChatId(tgChatId));
        }
        Chat chat = new Chat();
        chat.setTgChatId(tgChatId);
        chat.setCreatedAt(now());
        chatRepository.save(chat);
    }

    @Override
    public void unregister(long tgChatId) {
        chatRepository.findChatByTgChatId(tgChatId)
            .orElseThrow(
                () -> new UserNotFoundException(generateExceptionMessageForChatId(tgChatId))
            );
        chatRepository.deleteByTgChatId(tgChatId);

    }

    @Override
    @Transactional
    public boolean isRegistered(long tgChatId) {
        return chatRepository.findChatByTgChatId(tgChatId).isPresent();
    }
}
