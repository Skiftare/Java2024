package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.web.WebClientForScrapperCommunication;
import edu.java.bot.commands.entities.UntrackCommand;
import edu.java.bot.memory.DataManager;
import edu.java.bot.memory.DialogManager;
import java.security.SecureRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UntrackCommandTest {

    private UntrackCommand testingCommand;
    private final DialogManager manager =
        new DialogManager(new DataManager(new WebClientForScrapperCommunication("http://localhost:8080")));

    @BeforeEach
    public void setUp() {
        testingCommand = new UntrackCommand(manager);
    }

    @Test
    public void testThatGetCommandCommandAndReturnedThatThisIsUntrackCommand() {
        assertEquals("/untrack", testingCommand.getCommandName());
    }

    @Test
    public void testThatGetCommandDescriptionAndReturnedThatThisIsUntrackCommand() {
        assertEquals("Прекратить отслеживание ссылки", testingCommand.description());
    }

    @Test
    public void testThatGetInvalidCommandAndReturnedErrorMessage() {
        SecureRandom secureRandom = new SecureRandom();

        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        com.pengrad.telegrambot.model.Chat chat = mock(com.pengrad.telegrambot.model.Chat.class);
        when(message.text()).thenReturn("/untrack www.example.com");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message()).thenReturn(message);

        String expectedTextMessage = "Ссылка невалидна или отсутсвует в отслеживаемых";
        SendMessage sendMessage = testingCommand.handle(update);
        assertEquals(sendMessage.getParameters().get("chat_id"), chatId);
        assertEquals(sendMessage.getParameters().get("text"), expectedTextMessage);
    }
}

