package edu.java.api.web;

import edu.java.api.entities.requests.AddLinkRequest;
import edu.java.api.entities.requests.RemoveLinkRequest;
import edu.java.api.entities.responses.LinkResponse;
import edu.java.api.entities.responses.ListLinksResponse;
import edu.java.api.entities.responses.TgChatInteractionResponse;
import edu.java.database.DataOperationService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinksController {
    private final DataOperationService dataService;

    private boolean isValidId(long id) {
        return id > 0;
    }

    private static final String INVALID_ID = "Неверный ID чата";

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<TgChatInteractionResponse> registerChat(@PathVariable("id") Long id) {
        boolean wasOpeartionSuccess = false;
        TgChatInteractionResponse result;
        if (isValidId(id)) {
            if (!dataService.checkExistingOfChat(id)) {
                dataService.registerChat(id);
                result = new TgChatInteractionResponse(id, "Чат зарегистрирован");
                wasOpeartionSuccess = true;
            } else {
                result = new TgChatInteractionResponse(
                    id, "Чат уже зарегестрован, повторная регистрация ни к чему не приведёт"
                );
            }
        } else {
            result = new TgChatInteractionResponse(id, INVALID_ID);
        }

        if (wasOpeartionSuccess) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<TgChatInteractionResponse> deleteChat(@PathVariable("id") Long id) {
        boolean wasOpeartionSuccess = false;
        TgChatInteractionResponse result;
        if (isValidId(id)) {
            if (dataService.checkExistingOfChat(id)) {
                dataService.deleteChat(id);
                result = new TgChatInteractionResponse(id, "Чат удален");
                wasOpeartionSuccess = true;
                //return "Чат успешно удалён";
            } else {
                result = new TgChatInteractionResponse(id, "Чат не зарегистрован. Удалять то, чего нет, мы не умеем");
                //return "Чата нет, ничего не удаляем";
            }
        } else {
            result = new TgChatInteractionResponse(id, INVALID_ID);
        }
        if (wasOpeartionSuccess) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/tg-chat/{id}")
    public ResponseEntity<TgChatInteractionResponse> isChatRegistred(@PathVariable("id") Long id) {

        boolean wasOpeartionSuccess = false;
        TgChatInteractionResponse result;
        if (isValidId(id)) {
            if (dataService.checkExistingOfChat(id)) {
                result = new TgChatInteractionResponse(id, "Чат есть");
                wasOpeartionSuccess = true;
            } else {
                result = new TgChatInteractionResponse(id, "Чата нет");
            }
        } else {
            result = new TgChatInteractionResponse(id, INVALID_ID);
        }
        if (wasOpeartionSuccess) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getAllLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        List<LinkResponse> links = new ArrayList<>(); // Получение списка ссылок
        int size = links.size();
        ListLinksResponse response = new ListLinksResponse(links, size);

        if (isValidId(tgChatId)) {
            // Логика для получения всех отслеживаемых ссылок

            return ResponseEntity.ok(response);
        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody
    AddLinkRequest addLinkRequest
    ) {
        LinkResponse newLink = new LinkResponse(tgChatId, addLinkRequest.link());
        if (isValidId(tgChatId)) {
            // Логика для добавления новой ссылки

            return ResponseEntity.ok(newLink);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newLink);
        }
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody
    RemoveLinkRequest removeLinkRequest
    ) {
        LinkResponse removedLink = new LinkResponse(tgChatId, removeLinkRequest.link());
        if (isValidId(tgChatId)) {
            // Логика для удаления ссылки
            return ResponseEntity.ok(removedLink);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(removedLink);
        }
    }

}
