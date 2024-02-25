package edu.java.scrapper;

import edu.java.configuration.LinkUpdaterManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UpdaterTest {
        @Test
        public void shouldCorrectUpdate() {
            var linkUpdaterScheduler = Mockito.mock(LinkUpdaterManager.class);
            Mockito.doThrow(new RuntimeException()).when(linkUpdaterScheduler).update();

            Assertions.assertThrows(RuntimeException.class, linkUpdaterScheduler::update);
        }

}
