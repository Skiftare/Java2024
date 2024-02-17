package edu.java.bot.memory;

import edu.java.bot.processor.UserRequest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataManagerTest {


    @Test
    public void testAddURL() {
        // Arrange
        UserRequest userRequest = mock(UserRequest.class);
        when(userRequest.id()).thenReturn(1L);
        when(userRequest.message()).thenReturn("https://www.example.com");

        // Act
        boolean result = DataManager.addURl(userRequest);

        // Assert
        assertThat(result).isTrue();
        assertThat(DataManager.getListOFTrackedCommands(1L)).contains("https://www.example.com");
    }

    @Test
    public void testDeleteURL() {
        // Arrange
        UserRequest userRequest = mock(UserRequest.class);
        when(userRequest.id()).thenReturn(1L);
        when(userRequest.message()).thenReturn("https://www.example.com");

        // Add URL
        DataManager.addURl(userRequest);

        // Act
        boolean result = DataManager.deleteURl(userRequest);

        // Assert
        assertThat(result).isTrue();
        assertThat(DataManager.getListOFTrackedCommands(1L)).isEqualTo("Никаких ссылок не отслеживается");
    }

}
