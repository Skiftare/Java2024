package edu.java.bot.commands.loaders;

import edu.java.bot.commands.entities.Command;
import edu.java.bot.commands.entities.ListCommand;
import edu.java.bot.commands.entities.StartCommand;
import edu.java.bot.commands.entities.TrackCommand;
import edu.java.bot.commands.entities.UntrackCommand;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandLoaderForHelpMessage implements Loader {
    private static final String ENDL_CHAR = "\n";
    private static final String SEPARATOR_BETWEEN_COMMAND_AND_DESCRIPTION = " :\t";

    private final List<Command> commandList = new ArrayList<>();

    @Autowired public CommandLoaderForHelpMessage(
        StartCommand startCommand,
        ListCommand listCommand,
        TrackCommand trackCommand,
        UntrackCommand untrackCommand
    ) {

        commandList.add(startCommand);
        commandList.add(untrackCommand);
        commandList.add(listCommand);
        commandList.add(trackCommand);
    }

    @Override
    public List<Command> getCommandsList() {
        return commandList;
    }

    public String getCommandsWithDescription() {

        StringBuilder sb = new StringBuilder();
        for (Command command : commandList) {
            sb.append(command.getCommandName());
            sb.append(SEPARATOR_BETWEEN_COMMAND_AND_DESCRIPTION);
            sb.append(command.description());
            sb.append(ENDL_CHAR);
        }

        return sb.toString();
    }

}
