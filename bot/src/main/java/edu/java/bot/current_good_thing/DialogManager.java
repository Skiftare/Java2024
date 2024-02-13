package edu.java.bot.current_good_thing;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.ApplicationConfig;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class DialogManager {

    private final Map<Long, DialogState> activeDialogs;
    private final UserRepository userRepository;

    public DialogManager(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.activeDialogs = new HashMap<>();
    }

    public void processUpdate(Update update) {
        Long chatId = update.message().chat().id();
        String messageText = update.message().text();

        DialogState dialogState = activeDialogs.getOrDefault(chatId, DialogState.DEFAULT);

        switch (dialogState) {
            case TRACK_COMMAND:
                handleTrackCommand(chatId, messageText);
                break;
            case UNTRACK_COMMAND:
                handleUntrackCommand(chatId, messageText);
                break;
            default:
                handleDefaultCommand(chatId, messageText);
        }
    }

    private void handleTrackCommand(Long chatId, String messageText) {
        try {
            URL url = new URL(messageText);
            if (!checkLinkForPertinence(url)) {
                sendErrorMessage(chatId, "Invalid URL");
                return;
            }
            // Save the tracked URL to the repository
            //userRepository.addTrackedUrl(chatId, url);
            sendSuccessMessage(chatId, "URL successfully tracked");
        } catch (MalformedURLException e) {
            sendErrorMessage(chatId, "Invalid URL");
        }
        activeDialogs.remove(chatId);
    }

    private void handleUntrackCommand(Long chatId, String messageText) {
        try {
            URL url = new URL(messageText);
            // Remove the URL from the repository
            //userRepository.removeTrackedUrl(chatId, url);
            sendSuccessMessage(chatId, "URL successfully untracked");
        } catch (MalformedURLException e) {
            sendErrorMessage(chatId, "Invalid URL");
        }
        activeDialogs.remove(chatId);
    }

    private void handleDefaultCommand(Long chatId, String messageText) {
        if (messageText.equals("/track")) {
            activeDialogs.put(chatId, DialogState.TRACK_COMMAND);
            sendPromptMessage(chatId, "Please enter the URL you want to track");
        } else if (messageText.equals("/untrack")) {
            activeDialogs.put(chatId, DialogState.UNTRACK_COMMAND);
            sendPromptMessage(chatId, "Please enter the URL you want to untrack");
        } else {
            sendUnknownCommandMessage(chatId);
        }
    }

    private void sendErrorMessage(Long chatId, String errorMessage) {
        // Send an error message to the user
        SendMessage request = new SendMessage(chatId, "Error: " + errorMessage);
        //telegramBot.execute(request);
    }

    private void sendSuccessMessage(Long chatId, String successMessage) {
        // Send a success message to the user
        SendMessage request = new SendMessage(chatId, "Success: " + successMessage);
        //telegramBot.execute(request);
    }

    private void sendPromptMessage(Long chatId, String prompt) {
        // Send a prompt message to the user
        SendMessage request = new SendMessage(chatId, prompt);
        //telegramBot.execute(request);
    }

    private void sendUnknownCommandMessage(Long chatId) {
        // Send an unknown command message to the user
        SendMessage request = new SendMessage(chatId, "Unknown command");
        //telegramBot.execute(request);
    }

    private static boolean checkLinkForPertinence(URL link) {
        // Check if the link is valid
        String protocol = link.getProtocol();
        return protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https");
    }

    private enum DialogState {
        DEFAULT, TRACK_COMMAND, UNTRACK_COMMAND
    }
}
