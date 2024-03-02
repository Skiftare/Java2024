package edu.java.bot.memory;

import edu.java.bot.processor.UserRequest;

public interface DataManager {
    static boolean  addURl(UserRequest update) {
        return false;
    }

    static boolean deleteURl(UserRequest update) {
        return false;
    }
    static String getListOFTrackedCommands(Long id) {
        return "";
    }
}
