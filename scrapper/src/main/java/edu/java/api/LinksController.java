package edu.java.api;

import edu.java.data.request.AddLinkRequest;
import edu.java.data.request.RemoveLinkRequest;
import edu.java.data.response.LinkResponse;
import edu.java.data.response.ListLinksResponse;
import edu.java.data.response.ListOfLinksResponse;
import edu.java.data.response.ResultOfServiceOperation;
import edu.java.data.response.TgChatInteractionResponse;
import edu.java.exceptions.RequestProcessingException;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        LoggerFactory.getLogger(LinksController.class).info("Get links request for chat with id: " + tgChatId);
        ListOfLinksResponse result = methodProcessingService.getLinks(tgChatId); // Получение списка ссылок
        ListLinksResponse wrappedResult = new ListLinksResponse(
            result.resultList(), result.resultList().size()
        );
        return ResponseEntity.ok(wrappedResult);

    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") @Positive(message = INVALID_ID) Long tgChatId,
        @RequestBody
        AddLinkRequest addLinkRequest
    ) throws RequestProcessingException {
        LoggerFactory.getLogger(LinksController.class)
            .info("Add link request: " + addLinkRequest.link());
        LinkResponse result = methodProcessingService.addLink(tgChatId, addLinkRequest.link());
        LinkResponse wrappedResult = new LinkResponse(result.id(), result.url());

        return ResponseEntity.ok(wrappedResult);

    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") @Positive(message = INVALID_ID) Long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) throws RequestProcessingException {
        LoggerFactory.getLogger(LinksController.class)
            .info("Remove link request: " + removeLinkRequest.link());
        LinkResponse result = methodProcessingService.removeLink(tgChatId, removeLinkRequest.link());
        LinkResponse wrappedResult = new LinkResponse(result.id(), result.url());
        LoggerFactory.getLogger(LinksController.class)
            .info("Link removed: " + wrappedResult.url());
        return ResponseEntity.ok(wrappedResult);

    }

}
