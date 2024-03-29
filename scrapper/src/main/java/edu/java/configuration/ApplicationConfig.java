package edu.java.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    /*@Bean
    AccessType databaseAccessType,
*/
    @Bean
    @NotNull
    Scheduler scheduler,

    @NotNull
    StackOverflow stackOverflow,

    @NotNull
    GitHub gitHub

) {

    public record Scheduler(
        boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay, @NotNull Duration parserDelay
    ) {
    }

    public record StackOverflow(@NotBlank String defaultUrl, String configUrl) {
    }

    public record GitHub(@NotBlank String defaultUrl, String configUrl) {
    }
}
