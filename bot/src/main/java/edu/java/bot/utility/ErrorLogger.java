package edu.java.bot.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("HideUtilityClassConstructor")
public class ErrorLogger {
    private final static Logger LOGGER = LogManager.getLogger();
    public ErrorLogger(){
        return;
    }

    public static void createLogError(String error) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        StackTraceElement element = stackTraceElements[2];
        String logBuilder = element.getClassName() + "->"
            + element.getMethodName()
            + "| "
            + error;
        LOGGER.info(logBuilder);

    }

    public static void createLog(String s) {
        LOGGER.info(s);
    }
}
