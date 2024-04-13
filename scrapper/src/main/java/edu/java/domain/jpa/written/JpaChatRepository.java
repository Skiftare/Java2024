package edu.java.domain.jpa.written;

import edu.java.domain.jpa.dao.Chat;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findChatByTgChatId(long tgChatId);

    @EntityGraph(attributePaths = "links")
    Optional<Chat> findChatWithLinkByTgChatId(long tgChatId);

    @EntityGraph(attributePaths = "links")
    Optional<Chat> findChatWithLinkById(long id);

    void deleteByTgChatId(long tgChatId);
}
