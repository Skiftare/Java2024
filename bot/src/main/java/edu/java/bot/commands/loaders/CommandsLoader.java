package edu.java.bot.commands.loaders;

import edu.java.bot.commands.entities.Command;
import edu.java.bot.commands.entities.HelpCommand;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("HideUtilityClassConstructor")

public class CommandsLoader implements Loader{
    private static ApplicationContext applicationContext;



    private static final String PACKAGE_NAME = "edu.java.bot.commands.entities";

    private List<Command> downloadedClasses = new ArrayList<>();

    @Autowired
    CommandsLoader(
        HelpCommand helpCommand,
        CommandLoaderForHelpMessage extraLoader
        ){
        downloadedClasses = extraLoader.getCommandsList();
        downloadedClasses.add(helpCommand);
    }



    public List<String> getCommandsNames(){
        List<String> commandsNames = new ArrayList<>();
        for(Command command: downloadedClasses){
            commandsNames.add(command.getCommandName());
        }
        return commandsNames;
    }

    @Override
    public List<Command> getCommandsList() {
        return downloadedClasses;
    }





}
