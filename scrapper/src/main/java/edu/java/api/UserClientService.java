package edu.java.api;

import edu.java.data.response.LinkResponse;
import edu.java.data.response.ListOfLinksResponse;
import edu.java.data.response.ResultOfServiceOperation;
import edu.java.database.DatabaseOperations;
import edu.java.exceptions.RequestProcessingException;
import edu.java.exceptions.entities.LinkAlreadyExistException;
import edu.java.exceptions.entities.LinkNotFoundException;
import edu.java.exceptions.entities.UserAlreadyExistException;
import edu.java.exceptions.entities.UserNotFoundException;
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
            throw new UserNotFoundException("Пользователь не зарегистрирован");
        }
    }

    public ListOfLinksResponse getLinks(Long chatId) throws RequestProcessingException {
        isChatExist(chatId);
        return new ListOfLinksResponse(chatId, dataService.getLinks(chatId));

    }

    public LinkResponse addLink(Long chatId, URI link) throws RequestProcessingException {
        isChatExist(chatId);

        List<LinkResponse> linkResponses = dataService.getLinks(chatId);
        for (LinkResponse linkResponse : linkResponses) {
            if (URI.create(linkResponse.url()).getPath().equals(link.getPath())) {
                throw new LinkAlreadyExistException("Эта ссылка уже отслеживается");

            }
        }

        dataService.addLink(chatId, link);
        return new LinkResponse(chatId, link.toString());
    }

    public LinkResponse removeLink(Long chatId, URI link) throws RequestProcessingException {
        isChatExist(chatId);

        List<LinkResponse> linkResponses = dataService.getLinks(chatId);
        for (LinkResponse linkResponse : linkResponses) {
            if (URI.create(linkResponse.url()).getPath().equals(link.getPath())) {
                dataService.removeLink(chatId, link);
                return new LinkResponse(chatId, link.toString());
            }

        }
        throw new LinkNotFoundException("Данной ссылки нет в отслеживаемых");

    }

    ResultOfServiceOperation registerUser(Long id) throws RequestProcessingException {
        if (!dataService.checkExistingOfChat(id)) {
            dataService.registerChat(id);
            return new ResultOfServiceOperation(
                id,
                "Чат зарегистрирован"
            );
        }
        throw new UserAlreadyExistException("Чат уже зарегестрован, повторная регистрация ни к чему не приведёт");

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
