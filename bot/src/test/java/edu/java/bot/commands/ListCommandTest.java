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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListCommandTest {


    private ListCommand listCommand;
    private final DialogManager manager = new DialogManager(new DataManager(new WebClientForScrapperCommunication("http://localhost:8080")));


    @BeforeEach
    public void setUp() {
        listCommand = new ListCommand(manager);
    }

    @Test
    public void testThatGetCommandCommandAndReturnedThatThisIsListCommand() {
        assertEquals("/list", listCommand.getCommandName());
    }

    @Test
    public void testThatGetCommandDescriptionAndReturnedThatThisIsListCommand() {
        assertEquals("Показать список отслеживаемых ссылок", listCommand.description());
    }

}
