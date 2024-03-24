package edu.java.domain.jpa.written;

import edu.java.domain.jpa.dao.Chat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findChatByTgChatId(long tgChatId);

    @EntityGraph(attributePaths = "link")
    Optional<Chat> findChatWithLinkByTgChatId(long tgChatId);

    @EntityGraph(attributePaths = "link")
    Optional<Chat> findChatWithLinkById(long id);

    void deleteByTgChatId(long tgChatId);
}
