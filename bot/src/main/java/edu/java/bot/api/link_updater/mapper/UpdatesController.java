package edu.java.bot.api.link_updater.mapper;

import edu.java.data.request.LinkUpdateRequest;
import edu.java.exceptions.entities.RequestProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdatesController {
    private static final String SUCCESS_LINK_UPDATED = "Ссылка добавлена в очередь на парсинг";
    private static final String FAILED_LINKS_UPDATE =
        "Некорректные параметры запроса";
    private final UpdateManager updateManager;
    private final Logger logger = LoggerFactory.getLogger(UpdatesController.class);

    @PostMapping("/updates")
    public ResponseEntity<String> sendUpdate(
        @RequestBody
        LinkUpdateRequest linkUpdate
    )
        throws RequestProcessingException {
        logger.info("Server got an update request: " + linkUpdate.toString());
        if (updateManager.addRequest(linkUpdate)) {
            return ResponseEntity.ok(SUCCESS_LINK_UPDATED);
        } else {
            throw new RequestProcessingException(FAILED_LINKS_UPDATE);
        }
    }

}
