package edu.java.bot.commands.loaders;

import edu.java.bot.commands.entities.Command;
import edu.java.bot.commands.entities.ListCommand;
import edu.java.bot.commands.entities.StartCommand;
import edu.java.bot.commands.entities.TrackCommand;
import edu.java.bot.commands.entities.UntrackCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import static edu.java.bot.utility.UtilityStatusClass.ENDL_CHAR;
import static edu.java.bot.utility.UtilityStatusClass.SEPARATOR_BETWEEN_COMMAND_AND_DESCRIPTION;

@Component
public class CommandLoaderForHelpMessage implements Loader {
    private final List<Command> downloadedClasses = new ArrayList<>();

    @Autowired
    CommandLoaderForHelpMessage(
        StartCommand startCommand,
        ListCommand listCommand,
        TrackCommand trackCommand,
        UntrackCommand untrackCommand
    ){

        downloadedClasses.add(startCommand);
        downloadedClasses.add(untrackCommand);
        downloadedClasses.add(listCommand);
        downloadedClasses.add(trackCommand);

    }

    @Override
    public List<Command> getCommandsList() {
        return downloadedClasses;
    }

    public String getCommandsWithDescription() {

        StringBuilder sb = new StringBuilder();
        for (Command command : downloadedClasses) {
            sb.append(command.getCommandName());
            sb.append(SEPARATOR_BETWEEN_COMMAND_AND_DESCRIPTION);
            sb.append(command.description());
            sb.append(ENDL_CHAR);
        }

        return sb.toString();
    }

}
