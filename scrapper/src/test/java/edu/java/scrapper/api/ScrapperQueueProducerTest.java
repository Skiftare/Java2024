package edu.java.scrapper.api;

import edu.java.api.web.ScrapperQueueProducer;
import edu.java.data.request.LinkUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import java.net.URI;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ScrapperQueueProducerTest {

    @Mock
    private KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    private ScrapperQueueProducer scrapperQueueProducer;
    private static final String TOPIC = "zed-industries";
    private static final URI DEFAULT_URI = URI.create("https://github.com/zed-industries/zed");


    @BeforeEach
    public void setUp() {
        scrapperQueueProducer = new ScrapperQueueProducer(kafkaTemplate, TOPIC);
    }

    @Test
    public void testSendUpdate() {
        LinkUpdateRequest update = new LinkUpdateRequest(DEFAULT_URI, null, null);

        scrapperQueueProducer.sendUpdate(update);

        verify(kafkaTemplate, times(1)).send(TOPIC, update);
    }
}

