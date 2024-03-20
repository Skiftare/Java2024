package edu.java.scrapper.database;

import edu.java.domain.jdbc.services.JdbcChatService;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.exceptions.entities.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Rollback
public class JdbcChatServiceTest extends IntegrationTest {

    @Autowired
    private JdbcChatService jdbcChatService;

    @Test
    @Rollback
    public void testRegisterFail() {
        long tgChatId = 123456L;
        jdbcChatService.register(tgChatId);
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
