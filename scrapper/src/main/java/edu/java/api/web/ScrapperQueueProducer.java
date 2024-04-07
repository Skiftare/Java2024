package edu.java.api.web;

import edu.java.data.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class ScrapperQueueProducer implements UpdateClient {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final String topicName;

    @Override
    public void sendUpdate(LinkUpdateRequest update) {
        kafkaTemplate.send(topicName, update);
    }
}
