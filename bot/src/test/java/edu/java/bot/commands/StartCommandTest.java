package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.web.WebClientForScrapperCommunication;
import edu.java.bot.commands.entities.Command;
import edu.java.bot.commands.entities.StartCommand;
import edu.java.bot.memory.DataManager;
import edu.java.bot.memory.DialogManager;
import edu.java.bot.processor.DialogState;
import java.security.SecureRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StartCommandTest {
    private Command testingCommand;
    private final DialogManager manager = new DialogManager(new DataManager(new WebClientForScrapperCommunication("http://localhost:8080")));



    @BeforeEach
    public void setUp() {
        testingCommand = new StartCommand(manager);
    }

    @Test
    public void testThatGetCommandCommandAndReturnedThatThisIsStartCommand() {
        assertEquals("/start", testingCommand.getCommandName());
    }

    @Test
    public void testThatGetCommandDescriptionAndReturnedThatThisIsStartCommand() {
        assertEquals("Зарегистрировать пользователя", testingCommand.description());
    }

    @Test
    public void testThatGetCommandAndReturnedMessageForThatCommand() {
        SecureRandom secureRandom = new SecureRandom();
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        Message message = mock(Message.class);
        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);
        String expectedTextMessage = "Вы зарегистрированы!";

        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message()).thenReturn(message);

        //When: we execute update with this Command
        SendMessage sendMessage = testingCommand.handle(update);

        assertEquals(manager.getDialogState(chatId), DialogState.DEFAULT_SESSION);

        assertEquals(sendMessage.getParameters().get("chat_id"), chatId);
        assertEquals(sendMessage.getParameters().get("text"), expectedTextMessage);
    }
}
