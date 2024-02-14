package edu.java.bot.processor;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.HashMap;

@Service
public class DialogManager {
    private HashMap<Long, DialogState> activeDialogs = new HashMap<>();

    public static boolean isValidUrlForUntracking(URI url){
        return true;
    }
    public static boolean isValidUrlForTracking(URI irl){
        return true;
    }



}
