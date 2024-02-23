package edu.java.bot.commands;

import edu.java.bot.utility.ErrorLogger;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.UtilityStatusClass.ENDL_CHAR;
import static edu.java.bot.utility.UtilityStatusClass.FILE_EXTENSION_AS_CLASS;
import static edu.java.bot.utility.UtilityStatusClass.NAME_OF_COMMAND_METHOD_IN_CLASS_OF_BOT_COMMANDS;
import static edu.java.bot.utility.UtilityStatusClass.NAME_OF_DESCRIPTION_METHOD_IN_CLASS_OF_BOT_COMMANDS;
import static edu.java.bot.utility.UtilityStatusClass.POINT_CHAR_AS_STRING;
import static edu.java.bot.utility.UtilityStatusClass.SEPARATOR_BETWEEN_COMMAND_AND_DESCRIPTION;
import static edu.java.bot.utility.UtilityStatusClass.SLASH_CHAR_AS_STRING;
import static edu.java.bot.utility.UtilityStatusClass.STAR_CHAR_AS_STRING;



@Component
@SuppressWarnings("HideUtilityClassConstructor")

public class CommandsLoader implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    private static final String PACKAGE_NAME = "edu.java.bot.commands.entities";

    private static List<Class<?>> downloadedClasses = new ArrayList<>();

    public static String getCommandsWithDescription() {
        load();
        List<Class<?>> classes = getClasses();
        StringBuilder sb = new StringBuilder();
        for (Class<?> clazz : classes) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();

                sb.append(clazz.getMethod(NAME_OF_COMMAND_METHOD_IN_CLASS_OF_BOT_COMMANDS).invoke(instance));
                sb.append(SEPARATOR_BETWEEN_COMMAND_AND_DESCRIPTION);
                sb.append(clazz.getMethod(NAME_OF_DESCRIPTION_METHOD_IN_CLASS_OF_BOT_COMMANDS).invoke(instance));
                sb.append(ENDL_CHAR);
            } catch (Exception e) {
                ErrorLogger.createLogError(e.getMessage());
            }
        }
        return sb.toString();
    }

    public static List<String> getCommandsList() {
        load();
        List<Class<?>> classes = getClasses();
        List<String> list = new ArrayList<>();
        for (Class<?> clazz : classes) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                list.add((String) clazz.getMethod(NAME_OF_COMMAND_METHOD_IN_CLASS_OF_BOT_COMMANDS).invoke(instance));
            } catch (Exception e) {
                ErrorLogger.createLogError(e.getMessage());
            }
        }
        return list;
    }

    public static  List<Class<?>> getClasses() {
        if (downloadedClasses.isEmpty()) {
            load();
        }
        return downloadedClasses;
    }

    private static void load() {
        List<Class<?>> classes = new ArrayList<>();
        String packageName = "edu.java.bot.commands.entities";

        String[] beanNames = applicationContext.getBeanNamesForAnnotation(Component.class);
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            if (bean.getClass().getPackage().getName().startsWith(packageName)) {

                System.out.println(beanName);
                classes.add(bean.getClass());
            }
        }

        downloadedClasses = classes;
    }

}
