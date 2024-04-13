package edu.java.api;

import edu.java.data.request.AddLinkRequest;
import edu.java.data.request.RemoveLinkRequest;
import edu.java.data.response.LinkResponse;
import edu.java.data.response.ListOfLinksResponse;
import edu.java.data.response.ResultOfServiceOperation;
import edu.java.database.services.interfaces.LinkService;
import edu.java.database.services.interfaces.TgChatService;
import edu.java.exceptions.RequestProcessingException;
import edu.java.exceptions.entities.LinkAlreadyExistException;
import edu.java.exceptions.entities.LinkNotFoundException;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.exceptions.entities.UserNotFoundException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserClientService {
    private static final String LOGGING_USER_TEMPLATE = "At least, user {} is registered";
    private final TgChatService tgChatService;
    private final LinkService linkService;
    private final Logger logger = LoggerFactory.getLogger(UserClientService.class);

    private void isChatExist(Long chatId) throws RequestProcessingException {
        if (!tgChatService.isRegistered(chatId)) {
            throw new UserNotFoundException("Пользователь не зарегистрирован");
        }
    }

    public ListOfLinksResponse getLinks(Long chatId) throws RequestProcessingException {
        isChatExist(chatId);
        return new ListOfLinksResponse(chatId, linkService.listAll(chatId).links());

    }

    public LinkResponse addLink(Long chatId, URI link) throws RequestProcessingException {
        isChatExist(chatId);

        logger.info(LOGGING_USER_TEMPLATE, chatId);
        logger.info("Adding link: " + link.toString());
        List<LinkResponse> linkResponses = linkService.listAll(chatId).links();
        for (LinkResponse linkResponse : linkResponses) {
            if (URI.create(linkResponse.url()).getPath().equals(link.getPath())) {
                throw new LinkAlreadyExistException("Эта ссылка уже отслеживается");

            }
        }

        linkService.add(chatId, new AddLinkRequest(link));
        return new LinkResponse(chatId, link.toString());
    }

    public LinkResponse removeLink(Long chatId, URI link) throws RequestProcessingException {
        isChatExist(chatId);
        logger.info(LOGGING_USER_TEMPLATE, chatId);
        logger.info("Deleting link: " + link.toString());
        List<LinkResponse> linkResponses = linkService.listAll(chatId).links();
        for (LinkResponse linkResponse : linkResponses) {
            if (URI.create(linkResponse.url()).getPath().equals(link.getPath())) {
                linkService.remove(chatId, new RemoveLinkRequest(link));
                return new LinkResponse(chatId, link.toString());
            }

        }
        throw new LinkNotFoundException("Данной ссылки нет в отслеживаемых");

    }

    ResultOfServiceOperation registerUser(Long chatId) throws RequestProcessingException {
        if (!tgChatService.isRegistered(chatId)) {
            tgChatService.register(chatId);
            return new ResultOfServiceOperation(
                chatId,
                "Чат зарегистрирован"
            );
        }
        throw new UserAlreadyExistException("Чат уже зарегестрован, повторная регистрация ни к чему не приведёт");

    }

    ResultOfServiceOperation deleteUser(Long chatId) throws RequestProcessingException {
        isChatExist(chatId);

        tgChatService.unregister(chatId);
        return new ResultOfServiceOperation(chatId, "Чат удален");

    }

    ResultOfServiceOperation checkIsUserRegistered(Long chatId) throws RequestProcessingException {
        isChatExist(chatId);
        return new ResultOfServiceOperation(chatId, "Чат есть");
    }

}
