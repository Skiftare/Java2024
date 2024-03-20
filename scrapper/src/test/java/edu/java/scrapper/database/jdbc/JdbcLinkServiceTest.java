package edu.java.scrapper.database.jdbc;

import edu.java.data.request.AddLinkRequest;
import edu.java.domain.jdbc.services.JdbcChatService;
import edu.java.domain.jdbc.services.JdbcLinkService;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.scrapper.database.IntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Rollback
public class JdbcLinkServiceTest extends IntegrationTest {







/*
    @Test
    @Rollback
    public void testRegisterFail() {
        String link = "https://github.com/zed-industries/zed";
        long id = 123L;
        jdbcChatService.register(id);
        AddLinkRequest request = new AddLinkRequest(URI.create(TEST_LINK));
       // jdbcLinkService.add(id,request);
  //      assertThrows(UserAlreadyExistException.class, () -> jdbcLinkService.add(id, request));
    }
*/
}
