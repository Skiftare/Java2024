package edu.java.bot.processor;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.CommandsLoader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.*;
import java.util.ArrayList;

public class BotProcessorTest {



    @Test
    public void testRecognizeCommand_commandNotFound() throws Exception{
        // Given
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);

// Устанавливаем зависимости между объектами
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(123L);
        Mockito.when(message.text()).thenReturn("/unknown");

        Mockito.mockStatic(CommandsLoader.class);
        Mockito.when(CommandsLoader.getClasses()).thenReturn(new ArrayList<>());

        // When
        SendMessage response = BotProcessor.recognizeCommand(update);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getParameters().get("chat_id")).isEqualTo(chat.id());
    }

}
