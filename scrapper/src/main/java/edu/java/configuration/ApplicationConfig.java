package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Map;
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

    ServiceProperties github,
    ServiceProperties stackoverflow,
    ServiceProperties bot,
    RateLimitingSettings rateLimitingSettings,
    Kafka kafka,
    boolean useQueue

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
    public record RateLimitingSettings(int count, int tokens, Duration period) {
    }

    public record Kafka(String bootstrapServers, String topicName) {

    }
}
