package edu.java.bot.commands;

import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommandsLoader {
    private static final String PACKAGE_NAME = "edu.java.bot.commands.entities";

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
                e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        return list;
    }


    private static List<Class<?>> getClasses() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }
}
