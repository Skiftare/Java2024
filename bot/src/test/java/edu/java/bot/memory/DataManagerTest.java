package edu.java.bot.memory;

import edu.java.bot.processor.UserRequest;
import org.junit.Test;

import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataManagerTest {


    @Test
    public void testThatGetLinkForTrackAndReturnedSuccessTrackingOfIt() {
        // Given
        UserRequest userRequest = mock(UserRequest.class);
        SecureRandom secureRandom = new SecureRandom();
        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);
        when(userRequest.id()).thenReturn(chatId);
        when(userRequest.message()).thenReturn("https://www.example.com");

        // When
        boolean result = DataManager.addURl(userRequest);

        // Then
        assertThat(result).isTrue();
        assertThat(DataManager.getListOFTrackedCommands(1L)).contains("https://www.example.com");
    }

    @Test
    public void testThatGetLinkForUntrackAndReturnedSuccessUntrackingOfIt() {
        // Given
        UserRequest userRequest = mock(UserRequest.class);
        SecureRandom secureRandom = new SecureRandom();
        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);
        when(userRequest.id()).thenReturn(chatId);
        when(userRequest.message()).thenReturn("https://www.example.com");

        // When: add URL
        DataManager.addURl(userRequest);

        // Act
        boolean result = DataManager.deleteURl(userRequest);

        // Then
        assertThat(result).isTrue();
        assertThat(DataManager.getListOFTrackedCommands(1L)).isEqualTo("Никаких ссылок не отслеживается");
    }

}
