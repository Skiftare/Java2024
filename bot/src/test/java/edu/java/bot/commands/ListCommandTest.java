package edu.java.bot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.entities.ListCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.SecureRandom;

public class ListCommandTest {


    private ListCommand listCommand;

    @BeforeEach
    public void setUp() {
        listCommand = new ListCommand();
    }

    @Test
    public void testCommand() {
        assertEquals("/list", listCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("Показать список отслеживаемых ссылок", listCommand.description());
    }

    @Test
    public void testHandle() {
        SecureRandom secureRandom = new SecureRandom();
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);

        Message message = mock(Message.class);
        Long chatId = secureRandom.nextLong(0,Long.MAX_VALUE);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);

        SendMessage actualSendMessage = listCommand.handle(update);

        assertEquals(actualSendMessage.getParameters().get("chat_id"), chatId);
        assertEquals(actualSendMessage.getParameters().get("text"),"Никаких ссылок не отслеживается");


    }

}