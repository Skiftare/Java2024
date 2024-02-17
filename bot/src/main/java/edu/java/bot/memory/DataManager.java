package edu.java.bot.memory;

import com.pengrad.telegrambot.model.Update;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import org.springframework.stereotype.Repository;
import static edu.java.bot.utility.ErrorLogger.createLogError;
import static edu.java.bot.utility.UtilityStatusClass.ENDL_CHAR;
import static edu.java.bot.utility.UtilityStatusClass.NO_LINKS_NOT_TRACKED;

@Repository
public class DataManager {
    private static final HashMap<Long, HashSet<URI>> trackCashedMap = new HashMap<>();

    static boolean addURl(Update update) {
        Long id = update.message().chat().id();
        String url = update.message().text();
        HashSet<URI> urls = trackCashedMap.computeIfAbsent(id, k -> new HashSet<>());

        try {
            URI newUrl = new URI(url);

            urls.add(newUrl);
            trackCashedMap.put(id, urls);
            return true;
        } catch (URISyntaxException e) {
            createLogError(e.getMessage());
            return false;
        }
    }

    static boolean deleteURl(Update update) {
        Long id = update.message().chat().id();
        String url = update.message().text();
        HashSet<URI> urls = trackCashedMap.get(id);
        boolean result = false;

        try {
            URI urlToRemove = new URI(url);

            if (urls.remove(urlToRemove)) {
                if (urls.isEmpty()) {
                    trackCashedMap.remove(id);
                } else {
                    trackCashedMap.put(id, urls);
                }
                result = true;
            }
        } catch (URISyntaxException e) {
            createLogError(e.getMessage());
        }
        return result;
    }

    static String getListOFTrackedCommands(Long id) {
        String result = NO_LINKS_NOT_TRACKED;

        if (trackCashedMap.containsKey(id)) {

            HashSet<URI> urls = trackCashedMap.get(id);
            StringBuilder sb = new StringBuilder();
            for (URI url : urls) {
                sb.append(url.toString()).append(ENDL_CHAR);
            }
            result = sb.toString();
        }

        return result;
    }

}
