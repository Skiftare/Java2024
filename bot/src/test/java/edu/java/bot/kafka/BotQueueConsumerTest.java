package edu.java.bot.kafka;

import edu.java.bot.api.link_updater.mapper.UpdateManager;
import edu.java.bot.api.link_updater.mapper.UpdateQueueConsumer;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.data.request.LinkUpdateRequest;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BotQueueConsumerTest {
    private static final ApplicationConfig applicationConfig = new ApplicationConfig(
        null,
        null,
        null,
        new ApplicationConfig.Kafka(
            "updates",
            "bot",
            "localhost:9092",
            "badResponse"
        )
    );

    @Mock
    private UpdateManager updateManager;
    private static final URI DEFAULT_URI = URI.create("https://github.com/zed-industries/zed");

    @Mock
    private KafkaTemplate<String, LinkUpdateRequest> kafkaDlqTemplate;

    @InjectMocks
    private UpdateQueueConsumer botQueueConsumer;

    @Test
    public void testListen() {
        LinkUpdateRequest update = new LinkUpdateRequest(DEFAULT_URI, null, null);
        when(updateManager.addRequest(any())).thenReturn(true);

        botQueueConsumer.listen(update);

        verify(updateManager, times(1)).addRequest(update);
    }

    @Test
    public void testListenException() {
        LinkUpdateRequest update = new LinkUpdateRequest(DEFAULT_URI, null, null);
        doThrow(new RuntimeException()).when(updateManager).addRequest(any());
        botQueueConsumer = new UpdateQueueConsumer(applicationConfig, updateManager, kafkaDlqTemplate);

        botQueueConsumer.listen(update);

        ArgumentCaptor<LinkUpdateRequest> captor = ArgumentCaptor.forClass(LinkUpdateRequest.class);
        verify(kafkaDlqTemplate, times(1)).send(anyString(), captor.capture());

        assertThat(update).isEqualTo(captor.getValue());
    }
}
