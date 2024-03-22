package edu.java.api.web;

import edu.java.api.entities.exceptions.RequestProcessingException;
import edu.java.api.entities.responses.LinkResponse;
import edu.java.api.web.entities.LinkOperationResponse;
import edu.java.api.web.entities.ListOfLinksResponse;
import edu.java.api.web.entities.ResultOfServiceOperation;
import edu.java.database.DatabaseOperations;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserClientService {
    private final DatabaseOperations dataService;

    private void isChatExist(Long chatId) throws RequestProcessingException {
        if (!dataService.checkExistingOfChat(chatId)) {
            throw new RequestProcessingException("Пользователь не зарегистрирован");
        }
    }

    public ListOfLinksResponse getLinks(Long chatId) throws RequestProcessingException {
        isChatExist(chatId);
        return new ListOfLinksResponse(chatId, dataService.getLinks(chatId));

    }

    public LinkOperationResponse addLink(Long chatId, URI link) throws RequestProcessingException {
        isChatExist(chatId);

        List<LinkResponse> linkResponses = dataService.getLinks(chatId);
        for (LinkResponse linkResponse : linkResponses) {
            if (linkResponse.url().getPath().equals(link.getPath())) {
                throw new RequestProcessingException("Эта ссылка уже отслеживается");

            }
        }

        dataService.addLink(chatId, link);
        return new LinkOperationResponse(chatId, link);
    }

    public LinkOperationResponse removeLink(Long chatId, URI link) throws RequestProcessingException {
        isChatExist(chatId);

        List<LinkResponse> linkResponses = dataService.getLinks(chatId);
        for (LinkResponse linkResponse : linkResponses) {
            if (linkResponse.url().getPath().equals(link.getPath())) {
                dataService.removeLink(chatId, link);
                return new LinkOperationResponse(chatId, link);
            }

        }
        throw new RequestProcessingException("Данной ссылки нет в отслеживаемых");

    }

    ResultOfServiceOperation registerUser(Long id) throws RequestProcessingException {
        if (!dataService.checkExistingOfChat(id)) {
            dataService.registerChat(id);
            return new ResultOfServiceOperation(
                id,
                "Чат зарегистрирован"
            );
        }
        throw new RequestProcessingException("Чат уже зарегестрован, повторная регистрация ни к чему не приведёт");


    }

    ResultOfServiceOperation deleteUser(Long chatId) throws RequestProcessingException {
        isChatExist(chatId);

        dataService.deleteChat(chatId);
        return new ResultOfServiceOperation(chatId, "Чат удален");

    }

    ResultOfServiceOperation checkIsUserRegistered(Long chatId) throws RequestProcessingException {
        isChatExist(chatId);
        return new ResultOfServiceOperation(chatId, "Чат есть");

    }

}
