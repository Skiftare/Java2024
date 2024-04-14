package edu.java.bot.api.link_updater.mapper;

import edu.java.data.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class DlqConsumer {
    @KafkaListener(topics = "${app.kafka.topic-name}", groupId = "${app.kafka.consumer-group-id}")
    public void listen(LinkUpdateRequest update) {
        log.info(update.toString());
    }
}
