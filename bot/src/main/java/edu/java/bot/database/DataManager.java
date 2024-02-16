package edu.java.bot.database;

import com.pengrad.telegrambot.model.Update;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import org.springframework.stereotype.Repository;
import static edu.java.bot.utility.ErrorLogger.createLogError;

@Repository
public class DataManager {
    private static final HashMap<Long, HashSet<URL>> trackCashedMap = new HashMap<>();

    static boolean addURl(Update update) {
        Long id = update.message().chat().id();
        String url = update.message().text();
        HashSet<URL> urls = trackCashedMap.computeIfAbsent(id, k -> new HashSet<>());

        try {
            URL newUrl = new URL(url);

            urls.add(newUrl);
            trackCashedMap.put(id, urls);
            return true;
        } catch (MalformedURLException e) {
            createLogError(e.getMessage());
            return false;
        }
    }

    static boolean deleteURl(Update update) {
        Long id = update.message().chat().id();
        String url = update.message().text();
        HashSet<URL> urls = trackCashedMap.get(id);
        boolean result = false;

        try {
            URL urlToRemove = new URL(url);

            if (urls.remove(urlToRemove)) {
                if (urls.isEmpty()) {
                    trackCashedMap.remove(id);
                } else {
                    trackCashedMap.put(id, urls);
                }
                result = true;
            }
        } catch (MalformedURLException e) {
            createLogError(e.getMessage());
            result = false;
        }
        return result;
    }

    static String getListOFTrackedCommands(Long id) {
        String result = "Никаких ссылок не отслеживается";

        if (trackCashedMap.containsKey(id)) {

            HashSet<URL> urls = trackCashedMap.get(id);
            StringBuilder sb = new StringBuilder();
            for (URL url : urls) {
                sb.append(url.toString()).append('\n');
            }
            result = sb.toString();
        }

        return result;
    }

}
