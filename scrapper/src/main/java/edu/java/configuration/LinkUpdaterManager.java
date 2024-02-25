package edu.java.configuration;

import edu.java.utility.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "api.scheduler.enable", havingValue = "true")
public class LinkUpdaterManager {

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        logger.info("Updated smth");
    }
}
