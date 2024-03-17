package edu.java.bot.memory;

import edu.java.bot.api.entities.requests.AddLinkRequest;
import edu.java.bot.api.entities.requests.RemoveLinkRequest;
import edu.java.bot.api.entities.responses.LinkResponse;
import edu.java.bot.api.entities.responses.ListLinksResponse;
import edu.java.bot.api.web.WebClientForScrapperCommunication;
import edu.java.bot.processor.UserRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("HideUtilityClassConstructor")
public class DataManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);
    private static final String ENDL_CHAR = "\n";
    static final String NO_LINKS_NOT_TRACKED = "Никаких ссылок не отслеживается";

    private final WebClientForScrapperCommunication webClient;

    public DataManager(WebClientForScrapperCommunication webClient) {
        this.webClient = webClient;
    }

    boolean addURl(UserRequest update) {
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

    boolean deleteURl(UserRequest update) {
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

    String getListOFTrackedCommands(Long id) {
        String result = NO_LINKS_NOT_TRACKED;

        Optional<ListLinksResponse> linksResponse = webClient.getLinks(id);
        if (linksResponse.isPresent()) {
            StringBuilder sb = new StringBuilder();
            for (LinkResponse linkResponse : linksResponse.get().links()) {
                sb.append(linkResponse.url().toString()).append(ENDL_CHAR);
            }
            result = sb.toString();
        }

        return result;
    }

}
