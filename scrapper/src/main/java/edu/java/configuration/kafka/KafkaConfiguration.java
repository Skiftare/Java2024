package edu.java.configuration.kafka;

import edu.java.api.web.ScrapperQueueProducer;
import edu.java.api.web.UpdateClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.data.request.LinkUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class KafkaConfiguration {
    @Autowired
    private KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    @Autowired
    private ApplicationConfig applicationConfig;

    @Bean
    public UpdateClient scrapperQueueProducer() {
        String topicName = applicationConfig.kafka().topicName();
        return new ScrapperQueueProducer(kafkaTemplate, topicName);
    }
}
