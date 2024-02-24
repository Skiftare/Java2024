package edu.java.bot.memory;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.processor.DialogState;
import edu.java.bot.processor.UserRequest;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import static edu.java.bot.memory.DataManager.getListOFTrackedCommands;

@Service
@SuppressWarnings("HideUtilityClassConstructor")
public class DialogManager {
    private static final HashMap<Long, DialogState> ACTIVE_DIALOGS = new HashMap<>();
    private static final String SUCCESS_TRACK_INFO = "Отслеживание ссылки начато!";
    private static final String UNSUCCESSFUL_TRACK_INFO = "Ссылка невалидна";

    private static final String SUCCESS_UNTRACK_INFO = "Отслеживание ссылки прекращено!";
    private static final String UNSUCCESSFUL_UNTRACK_INFO = "Ссылка невалидна или отсутсвует в отслеживаемых";
    private static final String UNKNOWN_COMMAND_INFO = "Команда неизвестна";

    public static void setWaitForTrack(Long id) {
        ACTIVE_DIALOGS.put(id, DialogState.TRACK_URI);
    }

    public static void setWaitForUntrack(Long id) {
        ACTIVE_DIALOGS.put(id, DialogState.UNTRACK_URI);
    }

    public static void resetDialogState(Long id) {
        ACTIVE_DIALOGS.remove(id);
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

        return ACTIVE_DIALOGS.getOrDefault(id, DialogState.DEFAULT_SESSION);

    }

    public static SendMessage resolveCommandNeedCookie(UserRequest update) {
        SendMessage msg;

        if (ACTIVE_DIALOGS.containsKey(update.id())) {
            DialogState state = ACTIVE_DIALOGS.get(update.id());
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
