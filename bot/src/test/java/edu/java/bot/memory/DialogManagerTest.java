package edu.java.bot.memory;

import edu.java.bot.api.web.WebClientForScrapperCommunication;
import edu.java.bot.processor.UserRequest;
import java.security.SecureRandom;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DialogManagerTest {

    @Test
    public void testThatGetWrongCommandAndReturnedDefaultMessage() {
        // Given
        SecureRandom secureRandom = new SecureRandom();
        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);
        DialogManager manager = new DialogManager(new DataManager(new WebClientForScrapperCommunication(":")));

        UserRequest userRequest = mock(UserRequest.class);
        when(userRequest.id()).thenReturn(chatId);
        when(userRequest.message()).thenReturn("wrong command");

        // When
        Cookie message = manager.resolveCommandNeedCookie(userRequest);

        // Then
        assertThat(message.state()).isEqualTo(CookieState.INVALID_LINK);
    }
}
