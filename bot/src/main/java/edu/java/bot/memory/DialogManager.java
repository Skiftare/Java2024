package edu.java.bot.memory;

import edu.java.bot.processor.DialogState;
import edu.java.bot.processor.UserRequest;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import static edu.java.bot.memory.DataManager.NO_LINKS_NOT_TRACKED;

@Service
@SuppressWarnings("HideUtilityClassConstructor")
public class DialogManager {
    private static final String SUCCESS_TRACK_INFO = "Отслеживание ссылки начато!";
    private static final String UNSUCCESSFUL_TRACK_INFO = "Ссылка невалидна";

    private static final String SUCCESS_UNTRACK_INFO = "Отслеживание ссылки прекращено!";
    private static final String UNSUCCESSFUL_UNTRACK_INFO = "Ссылка невалидна или отсутсвует в отслеживаемых";
    private DataManager manager;

    public DialogManager(DataManager dataManager) {
        manager = dataManager;
    }

    public boolean setWaitForTrack(Long id) {
        if ( ALL_DIALOGS.containsKey(id)) {
            ALL_DIALOGS.put(id, DialogState.TRACK_URI);
            return true;
        }
        return false;
    }

    public boolean registerUser(Long id) {
        return manager.registerUser(id);
    }

    public void setWaitForUntrack(Long id) {
        ALL_DIALOGS.put(id, DialogState.UNTRACK_URI);
    }

    public void resetDialogState(Long id) {
        ALL_DIALOGS.put(id, DialogState.DEFAULT_SESSION);

    }

    public boolean trackURL(UserRequest update) {
        if (ALL_DIALOGS.containsKey(update.id())) {
            return manager.addURl(update);
        } else {
            return false;
        }
    }

    public boolean untrackURL(UserRequest update) {
        return manager.deleteURl(update);
    }

    public String getListOfTracked(UserRequest update) {
        if (ALL_DIALOGS.containsKey(update.id())) {
            return manager.getListOFTrackedCommands(update.id());
        } else {
            return NO_LINKS_NOT_TRACKED;
        }
    }

    public DialogState getDialogState(Long id) {
        return ALL_DIALOGS.getOrDefault(id, DialogState.NOT_REGISTERED);
    }

    public Cookie resolveCommandNeedCookie(UserRequest update) {
        if (WeakLinkChecker.checkLinkWithoutConnecting(update.message()) && ALL_DIALOGS.containsKey(update.id())) {
            DialogState state = ALL_DIALOGS.get(update.id());
            if (state == DialogState.TRACK_URI) {
                return new Cookie(update.id(), CookieState.WAITING_FOR_TRACK_URL);
            } else {
                return new Cookie(update.id(), CookieState.WAITING_FOR_UNTRACK_URL);
            }
        }

        return new Cookie(update.id(), CookieState.INVALID_LINK);
    }

}
