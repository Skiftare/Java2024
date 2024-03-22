package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.web.WebClientForScrapperCommunication;
import edu.java.bot.commands.entities.Command;
import edu.java.bot.commands.entities.HelpCommand;
import edu.java.bot.commands.entities.ListCommand;
import edu.java.bot.commands.entities.StartCommand;
import edu.java.bot.commands.entities.TrackCommand;
import edu.java.bot.commands.entities.UntrackCommand;
import edu.java.bot.commands.loaders.CommandLoaderForHelpMessage;
import edu.java.bot.memory.DataManager;
import edu.java.bot.memory.DialogManager;
import edu.java.bot.processor.DialogState;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;

import java.security.SecureRandom;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HelpCommandTest {
    private final DialogManager manager = new DialogManager(new DataManager(new WebClientForScrapperCommunication("http://localhost:8080")));

    private final Command helpCommand = new HelpCommand(new CommandLoaderForHelpMessage(
        new StartCommand(manager),
        new ListCommand(manager),
        new TrackCommand(manager),
        new UntrackCommand(manager)
    ), manager);


    @Test
    public void testThatGetCommandCommandAndReturnedThatThisIsHelpCommand() {
        assertEquals("/help", helpCommand.getCommandName());
    }

    @Test
    public void testThatGetCommandDescriptionAndReturnedThatThisIsHelpCommand() {
        assertEquals("Вывести окно с командами", helpCommand.description());
    }


    @Test
    @Rollback
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
        assertEquals(manager.getDialogState(chatId), DialogState.DEFAULT_SESSION);

        // Then help message go to right user and contain all commands
        assertEquals(sendMessage.getParameters().get("chat_id"), chatId);

        assertTrue(sendMessage.getParameters().get("text").toString().contains("Зарегистрировать пользователя"));
        assertTrue(sendMessage.getParameters().get("text").toString().contains("Прекратить отслеживание ссылки"));
        assertTrue(sendMessage.getParameters().get("text").toString().contains("Начать отслеживание ссылки"));
        assertTrue(sendMessage.getParameters().get("text").toString().contains("Показать список отслеживаемых ссылок"));

    }

}

