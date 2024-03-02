package edu.java.bot.api.link_updater.mapper;

import edu.java.bot.api.link_updater.request.RequestToUpdate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UpdateManager {
    private ArrayList<RequestToUpdate> currentListOfUpdates;
    boolean addRequest(RequestToUpdate req){
        if(currentListOfUpdates.contains(req)){
            return false;
        }
        else{
            currentListOfUpdates.add(req);
            return true;
        }
    }
}
