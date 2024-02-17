package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.entities.HelpCommand;
import edu.java.bot.memory.DialogManager;
import edu.java.bot.processor.DialogState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HelpCommandTest {

    private Command helpCommand;

    @BeforeEach
    public void setUp() {
        helpCommand = new HelpCommand();
    }

    @Test
    public void testCommand() {
        assertEquals("/help", helpCommand.command());
    }

    @Test
    public void testDescription() {
        assertEquals("Вывести окно с командами", helpCommand.description());
    }



    @Test
    public void testHandle() {
        // Create a mock Update object
        SecureRandom secureRandom = new SecureRandom();
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);

        Message message = mock(Message.class);
        Long chatId = secureRandom.nextLong(0,Long.MAX_VALUE);
        String expectedTextMessage = "/start :\tЗарегистрировать пользователя\n" +
            "/list :\tПоказать список отслеживаемых ссылок\n" +
            "/help :\tВывести окно с командами\n" +
            "/untrack :\tПрекратить отслеживание ссылки\n" +
            "/track :\tНачать отслеживание ссылки\n";


        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message()).thenReturn(message);


        // Create an instance of HelpCommand
        HelpCommand helpCommand = new HelpCommand();

        // Call handle method
        SendMessage sendMessage = helpCommand.handle(update);

        // Verify that the dialog state is reset
        assertEquals(DialogManager.getDialogState(chatId), DialogState.DEFAULT_SESSION);

        // Verify the response message
        assertEquals(sendMessage.getParameters().get("chat_id"), chatId);
        assertEquals(sendMessage.getParameters().get("text"),expectedTextMessage);
    }

}
