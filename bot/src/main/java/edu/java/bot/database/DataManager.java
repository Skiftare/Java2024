package edu.java.bot.database;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Repository;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class DataManager {
    private static final HashMap <Long, HashSet<URL>> trackCashedMap = new HashMap<>();

     public static boolean addURl(Update update){
         Long id = update.message().chat().id();
         String url = update.message().text();
         HashSet<URL> urls = trackCashedMap.get(id);

         if (urls == null) {
             urls = new HashSet<>();
             trackCashedMap.put(id, urls);
         }

         try {
             URL newUrl = new URL(url);

             urls.add(newUrl);
             trackCashedMap.put(id, urls);
             return true;
         } catch (MalformedURLException e) {
             return false;
         }
     }
     public static boolean deleteURl(Update update){
         Long id = update.message().chat().id();
         String url = update.message().text();
         HashSet<URL> urls = trackCashedMap.get(id);
         boolean result = false;

         if (urls == null) {
             return false;
         }

         try {
             URL urlToRemove = new URL(url);

             if (urls.remove(urlToRemove)) {
                 trackCashedMap.put(id, urls);
                 result = true;
             }
         } catch (MalformedURLException e) {

         }
         return result;
     }


}
