package edu.java.bot.memory;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.DialogState;
import edu.java.bot.processor.UserRequest;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import static edu.java.bot.memory.DataManager.NO_LINKS_NOT_TRACKED;

@Service
@SuppressWarnings("HideUtilityClassConstructor")
public class DialogManager {
    private static final HashMap<Long, DialogState> ALL_DIALOGS = new HashMap<>();
    private static final String SUCCESS_TRACK_INFO = "Отслеживание ссылки начато!";
    private static final String UNSUCCESSFUL_TRACK_INFO = "Ссылка невалидна";

    private static final String SUCCESS_UNTRACK_INFO = "Отслеживание ссылки прекращено!";
    private static final String UNSUCCESSFUL_UNTRACK_INFO = "Ссылка невалидна или отсутсвует в отслеживаемых";
    private static final String UNKNOWN_COMMAND_INFO = "Команда неизвестна";

    public static boolean setWaitForTrack(Long id) {
        if (ALL_DIALOGS.containsKey(id)) {
            ALL_DIALOGS.put(id, DialogState.TRACK_URI);
            return true;
        }
        return false;
    }

    public static boolean registerUser(Long id) {
        if (!ALL_DIALOGS.containsKey(id)) {
            ALL_DIALOGS.put(id, DialogState.DEFAULT_SESSION);
            return true;
        }
        return false;
    }

    public static void setWaitForUntrack(Long id) {
        if (ALL_DIALOGS.containsKey(id)) {
            ALL_DIALOGS.put(id, DialogState.UNTRACK_URI);
        }
    }

    public static void resetDialogState(Long id) {
        if (ALL_DIALOGS.containsKey(id)) {
            ALL_DIALOGS.put(id, DialogState.DEFAULT_SESSION);
        }
    }

    public static boolean trackURL(UserRequest update) {
        if (ALL_DIALOGS.containsKey(update.id())) {
            return DataManager.addURl(update);
        } else {
            return false;
        }
    }

    public static boolean untrackURL(UserRequest update) {
        return DataManager.deleteURl(update);
    }

    public static String getListOfTracked(UserRequest update) {
        if (ALL_DIALOGS.containsKey(update.id())) {
            return DataManager.getListOFTrackedCommands(update.id());
        } else {
            return NO_LINKS_NOT_TRACKED;
        }
    }

    public static DialogState getDialogState(Long id) {
        return ALL_DIALOGS.getOrDefault(id, DialogState.NOT_REGISTERED);
    }

    public static SendMessage resolveCommandNeedCookie(UserRequest update) {
        SendMessage msg = new SendMessage(update.id(), UNKNOWN_COMMAND_INFO);
        if (WeakLinkChecker.checkLinkWithoutConnecting(update.message()) && ALL_DIALOGS.containsKey(update.id())) {
            DialogState state = ALL_DIALOGS.get(update.id());
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
        }

        return msg;
    }

}
