package edu.java.scrapper.database.jpa;

import edu.java.data.request.AddLinkRequest;
import edu.java.domain.jpa.services.JpaChatService;
import edu.java.domain.jpa.services.JpaLinkService;
import edu.java.domain.jpa.written.JpaChatRepository;
import edu.java.domain.jpa.written.JpaLinkRepository;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.exceptions.entities.UserNotFoundException;
import edu.java.scrapper.database.IntegrationTest;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
public class JpaServiceTest extends IntegrationTest {

    private static final String TEST_LINK = "https://github.com/zed-industries/zed";

    @Autowired
    public JpaChatRepository chatRepository;

    @Autowired
    public JpaLinkRepository linkRepository;

    public JpaChatService jpaChatService;
    public JpaLinkService jpaLinkService;

    @BeforeEach
    public void setUp() {
        jpaChatService = new JpaChatService(chatRepository);

        jpaLinkService = new JpaLinkService(
            chatRepository,
            linkRepository
        );
    }

    @org.junit.jupiter.api.Nested
    class JpaChatServiceTest {

        @Test
        @Rollback
        public void testRegisterFail() {
            long tgChatId = 234L;
            jpaChatService.register(tgChatId);
            assertTrue(jpaChatService.isRegistered(tgChatId));
            assertThrows(UserAlreadyExistException.class, () -> jpaChatService.register(tgChatId));
        }

        @Test
        @Rollback
        public void testRegisterSucc() {
            long tgChatId = 236L;
            jpaChatService.register(tgChatId);
            assertTrue(jpaChatService.isRegistered(tgChatId));
        }

    }

    @org.junit.jupiter.api.Nested
    class JpaLinkServiceTest {

        @Test
        @Rollback
        public void testLinkRegisterError() {
            long id = 239L;
            AddLinkRequest request = new AddLinkRequest(URI.create(TEST_LINK));
            assertThrows(UserNotFoundException.class, () -> jpaLinkService.add(id, request));
        }
    }
}
