package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.CommandsLoader;
import edu.java.bot.utility.ErrorLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.SecureRandom;
import java.util.ArrayList;

import static edu.java.bot.utility.UtilityStatusClass.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class BotProcessorTest {

    @Test
    public void testThatGetUnknownCommandAndReturnedDefaultMessage() throws Exception {
        // Given
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        SecureRandom secureRandom = new SecureRandom();
        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);


        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(message.text()).thenReturn("unknown");

        // When
        SendMessage response = BotProcessor.recognizeCommand(update);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getParameters().get("chat_id")).isEqualTo(chat.id());
        Assertions.assertEquals(response.getParameters().get("text"), UNKNOWN_COMMAND_INFO);

    }

    @Test
    public void testThatGetWellKnownCommandAndReturnedDefaultMessageForThatCommand() {
        // Given
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.text()).thenReturn(START_COMMAND_COMMAND);
        Chat chat = Mockito.mock(Chat.class);
        SecureRandom secureRandom = new SecureRandom();
        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(update.message()).thenReturn(message);



        // When
        SendMessage response = BotProcessor.recognizeCommand(update);

        // Then
        assertThat(response.getParameters().get("chat_id")).isEqualTo(chat.id());
        assertThat(response.getParameters().get("text").toString()).isEqualTo(SUCCESS_START_INFO);
    }

}
