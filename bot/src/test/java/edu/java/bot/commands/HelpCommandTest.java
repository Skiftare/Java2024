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

import static edu.java.bot.utility.UtilityStatusClass.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HelpCommandTest {

    private final Command helpCommand = new HelpCommand();


    @Test
    public void testThatGetCommandCommandAndReturnedThatThisIsHelpCommand() {
        assertEquals("/help", helpCommand.command());
    }

    @Test
    public void testThatGetCommandDescriptionAndReturnedThatThisIsHelpCommand() {
        assertEquals("Вывести окно с командами", helpCommand.description());
    }


    @Test
    public void testThatGetCommandAndReturnedMessageForThatCommand() {
        // Given: setup
        SecureRandom secureRandom = new SecureRandom();
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);

        Message message = mock(Message.class);
        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);

        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(update.message()).thenReturn(message);


        //When: we execute update with this Command
        SendMessage sendMessage = helpCommand.handle(update);

        // Then dialog state is reset
        assertEquals(DialogManager.getDialogState(chatId), DialogState.DEFAULT_SESSION);

        // Then help message go to right user and contain all commands
        assertEquals(sendMessage.getParameters().get("chat_id"), chatId);

        assertTrue(sendMessage.getParameters().get("text").toString().contains(HELP_COMMAND_DESCRIPTION));
        assertTrue(sendMessage.getParameters().get("text").toString().contains(LIST_COMMAND_DESCRIPTION));
        assertTrue(sendMessage.getParameters().get("text").toString().contains(START_COMMAND_DESCRIPTION));
        assertTrue(sendMessage.getParameters().get("text").toString().contains(TRACK_COMMAND_DESCRIPTION));
        assertTrue(sendMessage.getParameters().get("text").toString().contains(UNTRACK_COMMAND_DESCRIPTION));

    }

}
