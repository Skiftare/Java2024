package edu.java.database.services.interfaces;

public interface LinkUpdater {
    int update();

    void checkForUpdates();

    default String generateExceptionMessage(String message) {
        return message;
    }
}
