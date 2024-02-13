package edu.java.bot.current_good_thing;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Component
public class UpdatesManager {
    private static DialogManager dialog;

    @PostConstruct
    void setUpUpdateManager(){
        DialogManager dialog = new DialogManager(new UserRepository());
    }

    private static boolean checkLinkForPertinence(URL link) {
        // Проверка на валидность ссылки
        String protocol = link.getProtocol();
        if (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https")) {
            return false;
        }
        return true;
    }

    private static int checkCommandForPertinence(String message) {
        // Проверка команды на соответствие доступным командам
        if (message.equals("/start") || message.startsWith("/help") || message.startsWith("/track") || message.startsWith("/untrack") || message.startsWith("/list")) {
            // Проверка на наличие ссылки после команды /track
            if (message.startsWith("/track") && message.length() > 6) {
                String urlSubstring = message.substring(6);
                try {
                    URL url = new URL(urlSubstring);
                    if (checkLinkForPertinence(url)) {
                        return 2; // Ссылка валидна
                    }
                } catch (MalformedURLException e) {
                    // Обработка неверного формата ссылки
                }
            }
            return 1; // Команда соответствует доступным командам, но без ссылки
        }

        return 0; // Неизвестная команда
    }
    //В целом, можем сказать, что на каждый запрос у нас есть ответ, причём не нулевой.
    // Так что если заставить dialog возвращать заготовку под сообщение,
    // а потом как-то кидать это обратно в бота - то ура, мы победили.
    public static int createUpdatesManager(List<Update> updates) {
        for (Update update : updates) {
            Long chatId = update.message().chat().id();
            //Тут проверка ссылок
            dialog.processUpdate(update);

        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
