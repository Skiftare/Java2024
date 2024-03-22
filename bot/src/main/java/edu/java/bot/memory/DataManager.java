package edu.java.bot.memory;

import edu.java.bot.processor.UserRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("HideUtilityClassConstructor")
public class DataManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);
    private static final HashMap<Long, HashSet<URI>> TRACK_CASHED_MAP = new HashMap<>();
    private static final String ENDL_CHAR = "\n";
    static final String NO_LINKS_NOT_TRACKED = "Никаких ссылок не отслеживается";

    static boolean addURl(UserRequest update) {
        Long id = update.id();

        String url = update.message();
        HashSet<URI> urls = TRACK_CASHED_MAP.computeIfAbsent(id, k -> new HashSet<>());

        try {
            URI newUrl = new URI(url);
            urls.add(newUrl);
            TRACK_CASHED_MAP.put(id, urls);
            return true;
        } catch (URISyntaxException e) {
            LOGGER.error("Ошибка при добавлении URL: {}", e.getMessage());
            return false;
        }
    }

    static boolean deleteURl(UserRequest update) {
        Long id = update.id();
        String url = update.message();
        HashSet<URI> urls = TRACK_CASHED_MAP.get(id);

        boolean result = false;

        try {
            URI urlToRemove = new URI(url);
            if (urls.remove(urlToRemove)) {
                if (urls.isEmpty()) {
                    TRACK_CASHED_MAP.remove(id);
                } else {
                    TRACK_CASHED_MAP.put(id, urls);
                }
                result = true;
            }
        } catch (URISyntaxException e) {
            LOGGER.error("Ошибка при удалении URL: {}", e.getMessage());
        }

        return result;
    }

    static String getListOFTrackedCommands(Long id) {
        String result = NO_LINKS_NOT_TRACKED;

        if (TRACK_CASHED_MAP.containsKey(id)) {
            HashSet<URI> urls = TRACK_CASHED_MAP.get(id);
            StringBuilder sb = new StringBuilder();
            for (URI url : urls) {
                sb.append(url.toString()).append(ENDL_CHAR);
            }
            result = sb.toString();
        }

        return result;
    }

}
