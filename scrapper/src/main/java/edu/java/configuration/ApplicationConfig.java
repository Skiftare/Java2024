package edu.java.configuration;

import edu.java.backoff_policy.RetryType;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(

    AccessType databaseAccessType,

    @Bean
    @NotNull
    Scheduler scheduler,

    ListOfSupportedLinks listOfLinksSupported,

    @NotNull
    Api api,
    ServiceProperties bot,
    ServiceProperties github,
    ServiceProperties stackoverflow
) {

    public record Scheduler(
        boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay, @NotNull Duration parserDelay
    ) {
    }

    public record ListOfSupportedLinks(String stackoverflow, String github) {
    }

    public record Api(String botUrl) {
    }

    public record ServiceProperties(
        RetryType retryType,
        int retryCount,
        Duration delay,
        Set<HttpStatus> retryCodes
    ) {
    }
}
