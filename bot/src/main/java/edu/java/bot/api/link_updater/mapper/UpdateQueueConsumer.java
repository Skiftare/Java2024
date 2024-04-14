package edu.java.bot.api.link_updater.mapper;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.data.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateQueueConsumer {
    private final ApplicationConfig applicationConfig;
    private final UpdateManager updateManager;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaDlqTemplate;

    @KafkaListener(topics = "${app.kafka.topic-name}", groupId = "${app.kafka.consumer-group-id}")
    public void listen(LinkUpdateRequest update) {
        try {
            updateManager.addRequest(update);
        } catch (Exception e) {
            var kafka = applicationConfig.kafka();
            kafkaDlqTemplate.send(kafka.topicName(), update);
        }
    }
}
