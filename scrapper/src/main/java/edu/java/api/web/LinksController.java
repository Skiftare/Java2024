package edu.java.api.web;

import edu.java.api.entities.requests.AddLinkRequest;
import edu.java.api.entities.requests.RemoveLinkRequest;
import edu.java.api.entities.responses.LinkResponse;
import edu.java.api.entities.responses.ListLinksResponse;
import edu.java.api.entities.responses.TgChatInteractionResponse;
import edu.java.database.DatabaseOperations;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.constraints.Positive;
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
    private final DatabaseOperations dataService;
    private final UserClientService methodProcessingService;

    private static final String INVALID_ID = "Неверный ID чата";

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<TgChatInteractionResponse> registerChat(
        @PathVariable("id") @Positive(message = INVALID_ID) Long id
    ) {
        ResultOfServiceOperation result = methodProcessingService.registerUser(id);
        TgChatInteractionResponse wrappedResult = new TgChatInteractionResponse(
            result.chatId(),
            result.message()
        );
        if (result.wasSuccessful()) {
            return ResponseEntity.ok(wrappedResult);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(wrappedResult);
        }
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<TgChatInteractionResponse> deleteChat(
        @PathVariable("id") @Positive(message = INVALID_ID) Long id
    ) {
        ResultOfServiceOperation result = methodProcessingService.deleteUser(id);
        TgChatInteractionResponse wrappedResult = new TgChatInteractionResponse(
            result.chatId(),
            result.message()
        );
        if (result.wasSuccessful()) {
            return ResponseEntity.ok(wrappedResult);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(wrappedResult);
        }
    }

    @GetMapping("/tg-chat/{id}")
    public ResponseEntity<TgChatInteractionResponse> isChatRegistered(
        @PathVariable("id") @Positive(message = INVALID_ID) Long id
    ) {

        ResultOfServiceOperation result = methodProcessingService.checkIsUserRegistered(id);
        TgChatInteractionResponse wrappedResult = new TgChatInteractionResponse(
            result.chatId(),
            result.message()
        );
        if (result.wasSuccessful()) {
            return ResponseEntity.ok(wrappedResult);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(wrappedResult);
        }

    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getAllLinks(
        @RequestHeader("Tg-Chat-Id") @Positive(message = INVALID_ID) Long tgChatId
    ) {
        List<LinkResponse> links = new ArrayList<>(); // Получение списка ссылок
        int size = links.size();
        ListLinksResponse response = new ListLinksResponse(links, size);

        // Логика для получения всех отслеживаемых ссылок

        return ResponseEntity.ok(response);

    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") @Positive(message = INVALID_ID) Long tgChatId, @RequestBody
    AddLinkRequest addLinkRequest
    ) {
        LinkResponse newLink = new LinkResponse(tgChatId, addLinkRequest.link());
        // Логика для добавления новой ссылки

        return ResponseEntity.ok(newLink);

    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") @Positive(message = INVALID_ID) Long tgChatId, @RequestBody
    RemoveLinkRequest removeLinkRequest
    ) {
        LinkResponse removedLink = new LinkResponse(tgChatId, removeLinkRequest.link());
        // Логика для удаления ссылки

        return ResponseEntity.ok(removedLink);

    }

}
