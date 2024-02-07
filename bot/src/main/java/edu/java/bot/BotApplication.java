package edu.java.bot;


import edu.java.bot.configuration.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    public static void main(String[] args) {
        //SpringApplication.run(BotApplication.class, args);

        SpringApplication application = new SpringApplication(BotApplication.class);
        application.addInitializers(new EnvConfig());
        ApplicationContext context = application.run(args);

        // Получаем экземпляр класса TelegramTokenPrinter из контекста
        TelegramTokenPrinter tokenPrinter = context.getBean(TelegramTokenPrinter.class);

        // Выводим значение токена на консоль
        tokenPrinter.printToken();


    }

    static class EnvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            Dotenv dotenv = Dotenv.configure().load();
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                applicationContext.getEnvironment().getSystemProperties().put(key, value);
            });
        }
    }


}


