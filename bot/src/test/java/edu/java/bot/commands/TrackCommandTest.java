package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.web.WebClientForScrapperCommunication;
import edu.java.bot.commands.entities.TrackCommand;
import edu.java.bot.memory.DataManager;
import java.security.SecureRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrackCommandTest {
    private TrackCommand testingCommand;
    private final DataManager manager =
        (new DataManager(new WebClientForScrapperCommunication("http://localhost:8080")));

    @BeforeEach
    public void setUp() {
        testingCommand = new TrackCommand(manager);
    }

    @Test
    public void testThatGetCommandCommandAndReturnedThatThisIsTrackCommand() {
        assertEquals("/track", testingCommand.getCommandName());
    }

    @Test
    public void testThatGetCommandDescriptionAndReturnedThatThisIsTrackCommand() {
        assertEquals("Начать отслеживание ссылки", testingCommand.description());
    }

    @Test
    public void testThatGetInvalidCommandAndReturnedErrorMessage() {
        //Given: setup
        SecureRandom secureRandom = new SecureRandom();
        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(message.text()).thenReturn("/track www.example.com");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message()).thenReturn(message);

        String expectedTextMessage = "Ссылка невалидна";

        //When: we execute update with this Command
        SendMessage sendMessage = testingCommand.handle(update);

        //Then: we get right answer
        assertEquals(sendMessage.getParameters().get("chat_id"), chatId);
        assertEquals(sendMessage.getParameters().get("text"), expectedTextMessage);
    }

}

