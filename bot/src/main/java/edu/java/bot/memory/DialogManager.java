package edu.java.bot.memory;

import edu.java.bot.processor.UserRequest;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("HideUtilityClassConstructor")
public class DialogManager {
    private static final String SUCCESS_TRACK_INFO = "Отслеживание ссылки начато!";
    private static final String UNSUCCESSFUL_TRACK_INFO = "Ссылка невалидна";

    private static final String SUCCESS_UNTRACK_INFO = "Отслеживание ссылки прекращено!";
    private static final String UNSUCCESSFUL_UNTRACK_INFO = "Ссылка невалидна или отсутсвует в отслеживаемых";
    private final DataManager manager;

    public DialogManager(DataManager dataManager) {
        manager = dataManager;
    }

    public boolean registerUser(Long id) {
        return manager.registerUser(id);
    }

    public boolean trackURL(UserRequest update) {
        return manager.addURl(update);

        //return false;

    }

    public boolean untrackURL(UserRequest update) {
        return manager.deleteURl(update);
    }

    public String getListOfTracked(UserRequest update) {

        return manager.getListOFTrackedCommands(update.id());

        //return NO_LINKS_NOT_TRACKED;

    }

}
