package edu.java.bot.commands;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static edu.java.bot.utility.ErrorLogger.createLogError;

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
                sb.append(clazz.getMethod("command").invoke(instance));
                sb.append("\s:\t");
                sb.append(clazz.getMethod("description").invoke(instance));
                sb.append('\n');
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
                list.add((String) clazz.getMethod("command").invoke(instance));
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
