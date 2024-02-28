package edu.java.bot.commands.loaders;

import edu.java.bot.commands.entities.Command;
import edu.java.bot.commands.entities.HelpCommand;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("HideUtilityClassConstructor")

public class CommandsLoader implements Loader {

    private final List<Command> commandList;

    @Autowired public CommandsLoader(
        HelpCommand helpCommand,
        CommandLoaderForHelpMessage extraLoader
    ) {
        commandList = extraLoader.getCommandsList();
        commandList.add(helpCommand);
    }

    public List<String> getCommandsNames() {
        List<String> commandsNames = new ArrayList<>();
        for (Command command : commandList) {
            commandsNames.add(command.getCommandName());
        }
        return commandsNames;
    }

    @Override
    public List<Command> getCommandsList() {
        return commandList;
    }

}
