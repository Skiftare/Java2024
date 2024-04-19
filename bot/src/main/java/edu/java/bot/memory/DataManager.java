package edu.java.bot.memory;

import edu.java.bot.api.web.WebClientForScrapperCommunication;
import edu.java.bot.processor.UserRequest;
import edu.java.data.request.AddLinkRequest;
import edu.java.data.request.RemoveLinkRequest;
import edu.java.data.response.LinkResponse;
import edu.java.data.response.ListLinksResponse;
import edu.java.exceptions.entities.UserNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("HideUtilityClassConstructor")
public class DataManager {
    static final String NO_LINKS_NOT_TRACKED = "Никаких ссылок не отслеживается";
    private static final String USER_DOES_NOT_EXISTS = "Пользователь не зарегистрирован";
    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);
    private static final String ENDL_CHAR = "\n";
    private final WebClientForScrapperCommunication webClient;

    public DataManager(WebClientForScrapperCommunication webClient) {
        this.webClient = webClient;
    }

    public boolean addURl(UserRequest update) {
        Long id = update.id();

        String url = update.message();

        try {
            URI newUrl = new URI(url);
            webClient.addLink(id, new AddLinkRequest(newUrl));
            return true;
        } catch (URISyntaxException e) {
            LOGGER.error("Ошибка при добавлении URL: {}", e.getMessage());
            return false;
        }
    }

    public String registerUser(Long id) {
        LOGGER.info("Registering user with id: {}", id);
        Optional<String> serverAnswer = webClient.registerChat(id);
        LOGGER.info("Server answer: {}", serverAnswer.orElse("No answer"));
        return serverAnswer.get();
    }

    public boolean deleteURl(UserRequest update) {
        Long id = update.id();
        String url = update.message();

        boolean result = false;

        try {
            URI urlToRemove = new URI(url);
            Optional<LinkResponse> serverAnswer = webClient.removeLink(id, new RemoveLinkRequest(urlToRemove));
            if (serverAnswer.isPresent()) {
                result = true;
            }
        } catch (URISyntaxException e) {
            LOGGER.error("Ошибка при удалении URL: {}", e.getMessage());
        }

        return result;
    }

    public String getListOFTrackedCommands(Long id) {
        String result = NO_LINKS_NOT_TRACKED;
        try {
            Optional<ListLinksResponse> linksResponse = webClient.getLinks(id);
            if (linksResponse.isPresent()) {
                StringBuilder sb = new StringBuilder();
                for (LinkResponse linkResponse : linksResponse.get().links()) {
                    sb.append(linkResponse.url()).append(ENDL_CHAR);
                }
                result = sb.toString();
            }
        } catch (UserNotFoundException e) {
            LOGGER.error("Пользователь не найден: {}", e.getMessage());
            return USER_DOES_NOT_EXISTS;
        }
        return result;
    }

}
