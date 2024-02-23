package edu.java.bot.configuration;

import edu.java.bot.commands.CommandsLoader;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    Long adminChatId
) {
}
/*
@Configuration
public class AppConfig {

    @Bean
    public CommandsLoader commandsLoader(ApplicationConfig applicationConfig) {
        return new CommandsLoader(applicationConfig);
    }

    @Bean
    public ApplicationConfig applicationConfig() {
        return new ApplicationConfig("your_telegram_token", 12345L);
    }
}
*/
