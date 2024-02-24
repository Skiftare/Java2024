package edu.java.bot.memory;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.DialogState;
import edu.java.bot.processor.UserRequest;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import static edu.java.bot.memory.DataManager.getListOFTrackedCommands;
import static edu.java.bot.utility.UtilityStatusClass.SUCCESS_TRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.SUCCESS_UNTRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.UNKNOWN_COMMAND_INFO;
import static edu.java.bot.utility.UtilityStatusClass.UNSUCCESSFUL_TRACK_INFO;
import static edu.java.bot.utility.UtilityStatusClass.UNSUCCESSFUL_UNTRACK_INFO;

@Service
@SuppressWarnings("HideUtilityClassConstructor")
public class DialogManager {
    private static HashMap<Long, DialogState> activeDialogs = new HashMap<>();

    public static void setWaitForTrack(Long id) {
        activeDialogs.put(id, DialogState.TRACK_URI);
    }

    public static void setWaitForUntrack(Long id) {
        activeDialogs.put(id, DialogState.UNTRACK_URI);
    }

    public static void resetDialogState(Long id) {
        activeDialogs.remove(id);
    }

    public static boolean trackURL(UserRequest update) {
        return DataManager.addURl(update);
    }

    public static boolean untrackURL(UserRequest update) {
        return DataManager.deleteURl(update);
    }

    public static String getListOfTracked(UserRequest update) {
        return getListOFTrackedCommands(update.id());
    }

    public static DialogState getDialogState(Long id) {

        return activeDialogs.getOrDefault(id, DialogState.DEFAULT_SESSION);

    }

    public static SendMessage resolveCommandNeedCookie(UserRequest update) {
        SendMessage msg;

        if (activeDialogs.containsKey(update.id())) {
            DialogState state = activeDialogs.get(update.id());
            if (state == DialogState.TRACK_URI) {
                msg = new SendMessage(
                    update.id(),
                    trackURL(update) ? SUCCESS_TRACK_INFO : UNSUCCESSFUL_TRACK_INFO
                );
            } else {
                msg = new SendMessage(
                    update.id(),
                    untrackURL(update) ? SUCCESS_UNTRACK_INFO : UNSUCCESSFUL_UNTRACK_INFO
                );

            }
        } else {
            msg = new SendMessage(update.id(), UNKNOWN_COMMAND_INFO);
        }

        return msg;
    }

}
