package edu.java.bot.memory;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.UserRequest;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DialogManagerTest {

    @Test
    public void testThatGetLinkForTrackAndReturnedSuccessTrackingOfIt() {
        // Given
        UserRequest userRequest = mock(UserRequest.class);
        when(userRequest.id()).thenReturn(1L);
        when(userRequest.message()).thenReturn("https://www.example.com");

        // When
        boolean result = DialogManager.trackURL(userRequest);

        // Then
        assertThat(result).isTrue();
        assertThat(DialogManager.getListOfTracked(userRequest)).contains("https://www.example.com");
    }

    @Test
    public void testThatGetLinkForUntrackAndReturnedSuccessUntrackingOfIt() {
        // Given
        UserRequest userRequest = mock(UserRequest.class);
        when(userRequest.id()).thenReturn(1L);
        when(userRequest.message()).thenReturn("https://www.example.com");

        // When
        DialogManager.trackURL(userRequest);

        boolean result = DialogManager.untrackURL(userRequest);

        // Then
        assertThat(result).isTrue();
        assertThat(DialogManager.getListOfTracked(userRequest)).isEqualTo("Никаких ссылок не отслеживается");
    }

    @Test
    public void testThatGetWrongCommandAndReturnedDefaultMessage() {
        // Given
        UserRequest userRequest = mock(UserRequest.class);
        when(userRequest.id()).thenReturn(1L);
        when(userRequest.message()).thenReturn("wrong command");

        // When
        SendMessage message = DialogManager.resolveProblemCommandNotFound(userRequest);

        // Then
        assertThat(message.getParameters().get("text")).isEqualTo("Команда неизвестна");
    }
}
