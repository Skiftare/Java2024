package edu.java.scrapper.database.jpa;

import edu.java.data.request.AddLinkRequest;
import edu.java.data.request.RemoveLinkRequest;
import edu.java.domain.jpa.services.JpaChatService;
import edu.java.domain.jpa.services.JpaLinkService;
import edu.java.exceptions.entities.LinkAlreadyExistException;
import edu.java.exceptions.entities.LinkNotFoundException;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.exceptions.entities.UserNotFoundException;
import edu.java.scrapper.database.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
public class JpaServiceTest extends IntegrationTest {

    private static final String TEST_LINK = "https://github.com/zed-industries/zed";

    @Autowired
    private JpaChatService jpaChatService;

    @Autowired
    private JpaLinkService jpaLinkService;

    @org.junit.jupiter.api.Nested
    class JpaChatServiceTest {

        @Test
        @Rollback
        public void testRegisterFail() {
            long tgChatId = 123456L;
            jpaChatService.register(tgChatId);
            assertTrue(jpaChatService.isRegistered(tgChatId));
            assertThrows(UserAlreadyExistException.class, () -> jpaChatService.register(tgChatId));
        }

        @Test
        @Rollback
        public void testUnregisterFail() {
            long tgChatId = 123455L;
            jpaChatService.register(tgChatId);
            jpaChatService.unregister(tgChatId);
            assertThrows(UserNotFoundException.class, () -> jpaChatService.unregister(tgChatId));
        }

        @Test
        @Rollback
        public void testRegisterSucc() {
            long tgChatId = 123457L;
            jpaChatService.register(tgChatId);
            assertTrue(jpaChatService.isRegistered(tgChatId));
        }

        @Test
        @Rollback
        public void testUnregisterSucc() {
            long tgChatId = 123459L;
            jpaChatService.register(tgChatId);
            jpaChatService.unregister(tgChatId);
            assertThrows(UserNotFoundException.class, () -> jpaChatService.unregister(tgChatId));
        }
    }

    @org.junit.jupiter.api.Nested
    class JpaLinkServiceTest {
        @Test
        @Rollback
        public void testLinkRegisterDoubleTimeError() {
            long id = 1238L;
            jpaChatService.register(id);
            AddLinkRequest request = new AddLinkRequest(URI.create(TEST_LINK));
            jpaLinkService.add(id, request);
            assertThrows(LinkAlreadyExistException.class, () -> jpaLinkService.add(id, request));
        }

        @Test
        @Rollback
        public void testLinkRegisterError() {
            long id = 1235L;
            AddLinkRequest request = new AddLinkRequest(URI.create(TEST_LINK));
            assertThrows(UserNotFoundException.class,()->jpaLinkService.add(id, request));
        }

        @Test
        @Rollback
        public void testLinkDeleteError() {
            long id = 1237L;
            jpaChatService.register(id);
            AddLinkRequest addRequest = new AddLinkRequest(URI.create(TEST_LINK));
            RemoveLinkRequest removeRequest = new RemoveLinkRequest(URI.create(TEST_LINK));
            jpaLinkService.add(id, addRequest);
            jpaLinkService.remove(id, removeRequest);
            assertThrows(LinkNotFoundException.class,()->jpaLinkService.remove(id, removeRequest));
        }
    }
}
