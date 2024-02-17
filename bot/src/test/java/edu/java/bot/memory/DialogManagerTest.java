package edu.java.bot.memory;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.UserRequest;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DialogManagerTest {

    @Test
    public void testTrackURL() {
        // Arrange
        UserRequest userRequest = mock(UserRequest.class);
        when(userRequest.id()).thenReturn(1L);
        when(userRequest.message()).thenReturn("https://www.example.com");

        // Act
        boolean result = DialogManager.trackURL(userRequest);

        // Assert
        assertThat(result).isTrue();
        assertThat(DialogManager.getListOfTracked(userRequest)).contains("https://www.example.com");
    }

    @Test
    public void testUntrackURL() {
        // Arrange
        UserRequest userRequest = mock(UserRequest.class);
        when(userRequest.id()).thenReturn(1L);
        when(userRequest.message()).thenReturn("https://www.example.com");

        // Add URL
        DialogManager.trackURL(userRequest);

        // Act
        boolean result = DialogManager.untrackURL(userRequest);

        // Assert
        assertThat(result).isTrue();
        assertThat(DialogManager.getListOfTracked(userRequest)).isEqualTo("Никаких ссылок не отслеживается");
    }

    @Test
    public void testResolveProblemCommandNotFound() {
        // Arrange
        UserRequest userRequest = mock(UserRequest.class);
        when(userRequest.id()).thenReturn(1L);
        when(userRequest.message()).thenReturn("wrong command");

        // Act
        SendMessage message = DialogManager.resolveProblemCommandNotFound(userRequest);

        // Assert
        assertThat(message.getParameters().get("text")).isEqualTo("Команда неизвестна");
    }
}
