package edu.java.bot.api.link_updater.mapper;

import edu.java.bot.api.entities.requests.LinkUpdate;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdatesController {
    private static final String SUCCESS_LINK_UPDATED = "Ссылка добавлена в очередь на парсинг";
    private static final String FAILED_LINKS_UPDATE =
        "Некорректные параметры запроса";
    private final UpdateManager updateManager;

    @PostMapping("/updates")
    public ResponseEntity<String> sendUpdate(@RequestBody LinkUpdate linkUpdate) {

        // логика обработки отправки обновления

        // Отправляем ответ в зависимости от обработки запроса
        if (updateManager.addRequest(linkUpdate)) {
            return ResponseEntity.ok(SUCCESS_LINK_UPDATED);
        } else {
           /* ApiErrorResponse errorResponse = new ApiErrorResponse(
                "Некорректные параметры запроса",
                "400",
                "mockedName",
                FAILED_LINKS_UPDATE,
                new ArrayList<>()
            );
            */
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FAILED_LINKS_UPDATE);
        }
    }

}
