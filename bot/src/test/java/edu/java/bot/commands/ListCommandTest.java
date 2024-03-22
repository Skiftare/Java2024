package edu.java.bot.commands;

import edu.java.bot.api.web.WebClientForScrapperCommunication;
import edu.java.bot.commands.entities.ListCommand;
import edu.java.bot.memory.DataManager;
import edu.java.bot.memory.DialogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCommandTest {

    private ListCommand listCommand;
    private final DialogManager manager =
        new DialogManager(new DataManager(new WebClientForScrapperCommunication("http://localhost:8080")));

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
