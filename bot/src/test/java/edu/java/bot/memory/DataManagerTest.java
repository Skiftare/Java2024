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
        // Given: DataManager & wrong link
        UserRequest userRequest = mock(UserRequest.class);
        SecureRandom secureRandom = new SecureRandom();
        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);
        when(userRequest.id()).thenReturn(chatId);
        when(userRequest.message()).thenReturn("https://www.example.com");

        // When: we try to add
        boolean result = DataManager.addURl(userRequest);

        // Then we can add, because checker was earlier
        assertThat(result).isTrue();
        assertThat(DataManager.getListOFTrackedCommands(chatId)).contains("https://www.example.com");
    }

    @Test
    public void testThatGetLinkForUntrackAndReturnedSuccessUntrackingOfIt() {
        // Given: DataManager & wrong link
        UserRequest userRequest = mock(UserRequest.class);
        SecureRandom secureRandom = new SecureRandom();
        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);
        when(userRequest.id()).thenReturn(chatId);
        when(userRequest.message()).thenReturn("https://www.example.com");


        DataManager.addURl(userRequest);

        // When: we try to delete this URL
        boolean result = DataManager.deleteURl(userRequest);

        // Then we can add, because we can add, because checker was earlier
        assertThat(result).isTrue();
        assertThat(DataManager.getListOFTrackedCommands(1L)).isEqualTo("Никаких ссылок не отслеживается");
    }

}
