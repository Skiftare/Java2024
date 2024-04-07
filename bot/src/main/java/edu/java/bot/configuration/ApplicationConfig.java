package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;
import java.util.Set;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    Api api,
    Kafka kafka,
    ServiceProperties scrapper
) {
    public record Api(String scrapperUrl) {

    }
    public record Kafka(String topicName, String consumerGroupId, String bootstrapServer, String typeMapping,
                         String dlqTopicName) {
    }
    public record ServiceProperties(
        RetryType retryType,
        int retryCount,
        Duration delay,
        Set<HttpStatus> retryCodes
    ) {
    }
}
