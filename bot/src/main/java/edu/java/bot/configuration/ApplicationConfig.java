package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    Api api,
    Kafka kafka
) {
    public record Api(String scrapperUrl) {

    }
    public record Kafka(String topicName, String consumerGroupId, String bootstrapServer, String typeMapping,
                        String dlqTopicName) {
    }
}
