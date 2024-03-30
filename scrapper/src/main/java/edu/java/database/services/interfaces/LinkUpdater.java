package edu.java.database.services.interfaces;

public interface LinkUpdater {

    void checkForUpdates();

    default String generateExceptionMessage(String message) {
        return message;
    }
}
