package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.HashMap;
import edu.java.bot.database.WeakLinkChecker;
import org.springframework.stereotype.Service;
import static edu.java.bot.utility.UtilityStatusClass.SUCCESS_TRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.SUCCESS_UNTRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.UNKNOWN_COMMAND_INFO;
import static edu.java.bot.utility.UtilityStatusClass.UNSUCCESSFUL_TRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.UNSUCCESSFUL_UNTRACK_INFO;

@Service
public class DialogManager {
    private static HashMap<Long, DialogState> activeDialogs = new HashMap<>();

    public static void setWaitForTrack(Long id) {
        activeDialogs.replace(id, DialogState.TRACK_URI);
    }

    public static void setWaitForUntrack(Long id) {
        activeDialogs.put(id, DialogState.UNTRACK_URI);
    }

    public static void resetDialogState(Long id) {
        activeDialogs.remove(id);
    }

    public static boolean trackURL(String message) {
        return true;
    }

    public static boolean untrackURL(String message) {
        return true;
    }

    public static SendMessage resolveProblemCommandNotFound(Update update) {
        SendMessage msg;
        if (activeDialogs.containsKey(update.message().chat().id()) && WeakLinkChecker.checkLinkWithoutConnecting(update.message().text())) {
            DialogState state = activeDialogs.get(update.message().chat().id());
            if (state == DialogState.TRACK_URI) {
                msg = new SendMessage(
                    update.message().chat().id(),
                    trackURL(update.message().text()) ?
                        SUCCESS_TRACK_INFO : UNSUCCESSFUL_TRACK_INFO
                );
            } else {
                msg = new SendMessage(
                    update.message().chat().id(),
                    untrackURL(update.message().text()) ?
                        SUCCESS_UNTRACK_INFO : UNSUCCESSFUL_UNTRACK_INFO
                );

            }
        }
        else  msg = new SendMessage(update.message().chat().id(), UNKNOWN_COMMAND_INFO);
        return msg;
    }


}
