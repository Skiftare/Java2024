package edu.java.domain.jpa.written;

import edu.java.domain.jpa.dao.Link;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findLinkByUrl(String url);

    @EntityGraph(attributePaths = "chats")
    Optional<Link> findLinkWithChatByUrl(String url);

    @EntityGraph(attributePaths = "chats")
    Optional<Link> findLinkWithChatById(long id);

    List<Link> findLinksByLastUpdateAtBefore(OffsetDateTime dateTime);

    @Modifying
    @Query("UPDATE Link l SET l.lastUpdateAt = :dateTime WHERE l.id = :id")
    void updateLastUpdateAtById(long id, OffsetDateTime dateTime);

    void deleteByUrl(String url);

    @Modifying
    @Query("DELETE FROM Link l WHERE NOT EXISTS ("
        + "SELECT 1 FROM Chat c JOIN c.links cl WHERE cl.id = l.id)"
    )
    void deleteUnnecessary();
}
