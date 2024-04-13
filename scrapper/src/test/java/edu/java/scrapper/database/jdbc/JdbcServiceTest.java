package edu.java.scrapper.database.jdbc;

import edu.java.data.request.AddLinkRequest;
import edu.java.data.request.RemoveLinkRequest;
import edu.java.domain.jdbc.services.JdbcChatService;
import edu.java.domain.jdbc.services.JdbcLinkService;
import edu.java.exceptions.entities.LinkAlreadyExistException;
import edu.java.exceptions.entities.LinkNotFoundException;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.exceptions.entities.UserNotFoundException;
import edu.java.scrapper.database.IntegrationTest;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Rollback
public class JdbcServiceTest extends IntegrationTest {
    private static final String TEST_LINK = "https://github.com/zed-industries/zed";

    @Autowired
    private JdbcChatService jdbcChatService;

    @Autowired
    private JdbcLinkService jdbcLinkService;

    @org.junit.jupiter.api.Nested
    class JdbcChatServiceTest {

        @Test
        @Rollback
        public void testRegisterFail() {
            long tgChatId = 123456L;
            jdbcChatService.register(tgChatId);
            assertTrue(jdbcChatService.isRegistered(tgChatId));
            assertThrows(UserAlreadyExistException.class, () -> jdbcChatService.register(tgChatId));
        }

        @Test
        @Rollback
        public void testUnregisterFail() {
            long tgChatId = 123455L;
            jdbcChatService.register(tgChatId);
            jdbcChatService.unregister(tgChatId);
            assertThrows(UserNotFoundException.class, () -> jdbcChatService.unregister(tgChatId));
        }

        @Test
        @Rollback
        public void testRegisterSucc() {
            long tgChatId = 123457L;
            jdbcChatService.register(tgChatId);
        }

        @Test
        @Rollback
        public void testUnregisterSucc() {
            long tgChatId = 123459L;
            jdbcChatService.register(tgChatId);
            jdbcChatService.unregister(tgChatId);
        }
    }

    @org.junit.jupiter.api.Nested
    class JdbcLinkServiceTest {
        @Test
        @Rollback
        public void testLinkRegisterDoubleTimeError() {
            long id = 1238L;
            jdbcChatService.register(id);
            AddLinkRequest request = new AddLinkRequest(URI.create(TEST_LINK));
            jdbcLinkService.add(id, request);
            assertThrows(LinkAlreadyExistException.class, () -> jdbcLinkService.add(id, request));
        }

        @Test
        @Rollback
        public void testLinkRegisterError() {
            long id = 1235L;
            AddLinkRequest request = new AddLinkRequest(URI.create(TEST_LINK));
            assertThrows(UserNotFoundException.class, () -> jdbcLinkService.add(id, request));
        }

        @Test
        @Rollback
        public void testLinkDeleteError() {
            long id = 1237L;
            jdbcChatService.register(id);
            AddLinkRequest addRequest = new AddLinkRequest(URI.create(TEST_LINK + "5"));
            RemoveLinkRequest removeRequest = new RemoveLinkRequest(URI.create(TEST_LINK + "5"));
            jdbcLinkService.add(id, addRequest);
            jdbcLinkService.remove(id, removeRequest);
            assertThrows(LinkNotFoundException.class, () -> jdbcLinkService.remove(id, removeRequest));
        }
    }

}
