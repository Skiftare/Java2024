package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.HashMap;

@Service
public class DialogManager {
    private static HashMap<Long, DialogState> activeDialogs = new HashMap<>();

    public static boolean isValidUrlForUntracking(URI url){
        return true;
    }
    public static boolean isValidUrlForTracking(URI irl){
        return true;
    }

    public static void markDialogForWaitngToTrack(Update update){
        activeDialogs.put(update.message().chat().id(), DialogState.TRACK_URI);
    }

    public static void markDialogForWaitngToUntrack(Update update){
        activeDialogs.put(update.message().chat().id(), DialogState.UNTRACK_URI);
    }



}
