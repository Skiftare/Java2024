package edu.java.bot.commands;

import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.ErrorLogger.createLogError;
import static edu.java.bot.utility.UtilityStatusClass.ENDL_CHAR;
import static edu.java.bot.utility.UtilityStatusClass.NAME_OF_COMMAND_METHOD_IN_CLASS_OF_BOT_COMMANDS;
import static edu.java.bot.utility.UtilityStatusClass.NAME_OF_DESCRIPTION_METHOD_IN_CLASS_OF_BOT_COMMANDS;
import static edu.java.bot.utility.UtilityStatusClass.SEPARATOR_BETWEEN_COMMAND_AND_DESCRIPTION;

@Component
public class CommandsLoader {
    private static final String PACKAGE_NAME = "edu.java.bot.commands.entities";

    private static List<Class<?>> downloadedClasses = new ArrayList<>();

    public static String getCommandsWithDescription(){
        List<Class<?>> classes = getClasses();
        StringBuilder sb = new StringBuilder();
        for(Class<?> clazz: classes){
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                sb.append(clazz.getMethod(NAME_OF_COMMAND_METHOD_IN_CLASS_OF_BOT_COMMANDS).invoke(instance));
                sb.append(SEPARATOR_BETWEEN_COMMAND_AND_DESCRIPTION);
                sb.append(clazz.getMethod(NAME_OF_DESCRIPTION_METHOD_IN_CLASS_OF_BOT_COMMANDS).invoke(instance));
                sb.append(ENDL_CHAR);
            } catch (Exception e) {
                createLogError(e.getMessage());
            }
        }
        return sb.toString();
    }

    public static List<String> getCommandsList(){
        List<Class<?>> classes = getClasses();
        List<String> list = new ArrayList<>();
        for(Class<?> clazz: classes){
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                list.add((String) clazz.getMethod(NAME_OF_COMMAND_METHOD_IN_CLASS_OF_BOT_COMMANDS).invoke(instance));
            } catch (Exception e) {
                createLogError(e.getMessage());
            }
        }
        return list;
    }




    public static List<Class<?>> getClasses() {
        if(downloadedClasses.isEmpty()){
            load();
        }
        return downloadedClasses;
    }
    private static void load() {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = PACKAGE_NAME.replace('.', '/');
            URL resource = classLoader.getResource(path);
            assert resource != null;
            Path dirPath = Paths.get(resource.toURI());

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.class")) {
                for (Path entry : stream) {
                    String className = PACKAGE_NAME + "." + entry.getFileName().toString().replace(".class", "");
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                }
            }
            catch (Exception e){
                createLogError(e.getMessage());
            }
        } catch (Exception e) {
            createLogError(e.getMessage());
        }

       downloadedClasses = classes;
    }


}
