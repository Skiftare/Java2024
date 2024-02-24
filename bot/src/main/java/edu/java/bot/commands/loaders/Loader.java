package edu.java.bot.commands.loaders;

import edu.java.bot.commands.entities.Command;
import java.util.List;

public interface Loader {
    List<Command> getCommandsList();

}
