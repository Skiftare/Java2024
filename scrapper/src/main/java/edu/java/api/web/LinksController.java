package edu.java.api.web;

import edu.java.api.entities.exceptions.RequestProcessingException;
import edu.java.api.entities.requests.AddLinkRequest;
import edu.java.api.entities.requests.RemoveLinkRequest;
import edu.java.api.entities.responses.LinkResponse;
import edu.java.api.entities.responses.ListLinksResponse;
import edu.java.api.entities.responses.TgChatInteractionResponse;
import edu.java.api.web.entities.LinkOperationResponse;
import edu.java.api.web.entities.ListOfLinksResponse;
import edu.java.api.web.entities.ResultOfServiceOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
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
    private static final String INVALID_ID = "Неверный ID чата";
    private final UserClientService methodProcessingService;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<TgChatInteractionResponse> registerChat(
        @PathVariable("id") @Positive(message = INVALID_ID) Long id
    ) throws RequestProcessingException {
        ResultOfServiceOperation result = methodProcessingService.registerUser(id);
        TgChatInteractionResponse wrappedResult = new TgChatInteractionResponse(result.chatId(), result.message());
        return ResponseEntity.ok(wrappedResult);

    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<TgChatInteractionResponse> deleteChat(
        @PathVariable("id") @Positive(message = INVALID_ID) Long id
    ) throws RequestProcessingException {
        ResultOfServiceOperation result = methodProcessingService.deleteUser(id);
        TgChatInteractionResponse wrappedResult = new TgChatInteractionResponse(result.chatId(), result.message());

        return ResponseEntity.ok(wrappedResult);

    }

    @GetMapping("/tg-chat/{id}")
    public ResponseEntity<TgChatInteractionResponse> isChatRegistered(
        @PathVariable("id") @Positive(message = INVALID_ID) Long id
    ) throws RequestProcessingException {
        ResultOfServiceOperation result = methodProcessingService.checkIsUserRegistered(id);
        TgChatInteractionResponse wrappedResult = new TgChatInteractionResponse(
            result.chatId(),
            result.message()
        );

        return ResponseEntity.ok(wrappedResult);

    }

    @GetMapping("/links")
    public ResponseEntity<ListLinksResponse> getAllLinks(
        @RequestHeader("Tg-Chat-Id") @Positive(message = INVALID_ID) Long tgChatId
    ) throws RequestProcessingException {

        ListOfLinksResponse result = methodProcessingService.getLinks(tgChatId); // Получение списка ссылок
        ListLinksResponse wrappedResult = new ListLinksResponse(
            result.resultList(), result.resultList().size()
        );
        return ResponseEntity.ok(wrappedResult);

    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") @Positive(message = INVALID_ID) Long tgChatId, @RequestBody
    AddLinkRequest addLinkRequest
    ) throws RequestProcessingException {

        LinkOperationResponse result = methodProcessingService.addLink(tgChatId, addLinkRequest.link());
        LinkResponse wrappedResult = new LinkResponse(result.chatId(), result.url());

        return ResponseEntity.ok(wrappedResult);

    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") @Positive(message = INVALID_ID) Long tgChatId, @RequestBody
    RemoveLinkRequest removeLinkRequest
    ) throws RequestProcessingException {

        LinkOperationResponse result = methodProcessingService.removeLink(tgChatId, removeLinkRequest.link());
        LinkResponse wrappedResult = new LinkResponse(result.chatId(), result.url());
        return ResponseEntity.ok(wrappedResult);

    }

}
