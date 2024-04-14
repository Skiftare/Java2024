package edu.java.bot.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.web.WebClientForScrapperCommunication;
import edu.java.bot.commands.entities.HelpCommand;
import edu.java.bot.commands.entities.ListCommand;
import edu.java.bot.commands.entities.StartCommand;
import edu.java.bot.commands.entities.TrackCommand;
import edu.java.bot.commands.entities.UntrackCommand;
import edu.java.bot.commands.loaders.CommandLoaderForHelpMessage;
import edu.java.bot.commands.loaders.CommandsLoader;
import edu.java.bot.memory.DataManager;
import edu.java.bot.memory.DialogManager;
import java.security.SecureRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

public class BotProcessorTest {

    @Test
    public void testThatGetUnknownCommandAndReturnedDefaultMessage() {
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
        TelegramBot mockBot = Mockito.mock(TelegramBot.class);
        WebClientForScrapperCommunication server = Mockito.mock(WebClientForScrapperCommunication.class);
        DialogManager dialogManager = new DialogManager(new DataManager(server));
        CommandLoaderForHelpMessage helper = new CommandLoaderForHelpMessage(
            new StartCommand(dialogManager),
            new ListCommand(dialogManager),
            new TrackCommand(dialogManager),
            new UntrackCommand(dialogManager)
        );

        CommandsLoader loader = new CommandsLoader(
            new HelpCommand(helper), helper
        );
        ReplyKeyboardMarkup mockKeyboard = Mockito.mock(ReplyKeyboardMarkup.class);

        BotProcessor bot = new BotProcessor(mockBot, loader, mockKeyboard);

        //When
        SendMessage response = bot.recognizeCommand(update);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getParameters().get("chat_id")).isEqualTo(chat.id());
        Assertions.assertEquals(response.getParameters().get("text"), "Неизвестная команда");

    }

    @Test
    public void testThatGetWellKnownCommandAndReturnedDefaultMessageForThatCommand() {
        // Given
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.text()).thenReturn("/start");
        Chat chat = Mockito.mock(Chat.class);
        SecureRandom secureRandom = new SecureRandom();
        Long chatId = secureRandom.nextLong(0, Long.MAX_VALUE);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(update.message()).thenReturn(message);
        TelegramBot mockBot = Mockito.mock(TelegramBot.class);
        WebClientForScrapperCommunication server = Mockito.mock(WebClientForScrapperCommunication.class);
        DialogManager dialogManager = new DialogManager(new DataManager(server));
        CommandLoaderForHelpMessage helper = new CommandLoaderForHelpMessage(
            new StartCommand(dialogManager),
            new ListCommand(dialogManager),
            new TrackCommand(dialogManager),
            new UntrackCommand(dialogManager)
        );

        CommandsLoader loader = new CommandsLoader(
            new HelpCommand(helper), helper
        );
        ReplyKeyboardMarkup mockKeyboard = Mockito.mock(ReplyKeyboardMarkup.class);

        BotProcessor bot = new BotProcessor(mockBot, loader, mockKeyboard);

        // When
        SendMessage response = bot.recognizeCommand(update);

        // Then
        assertThat(response.getParameters().get("chat_id")).isEqualTo(chat.id());
        assertThat(response.getParameters().get("text").toString()).isEqualTo(
            "Вы уже зарегистрированы, повторная регистрация не нужна");
    }

}
