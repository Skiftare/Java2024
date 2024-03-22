package edu.java.bot.api.link_updater.mapper;

import edu.java.bot.api.entities.requests.LinkUpdate;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class UpdateManager {
    private ArrayList<LinkUpdate> currentListOfUpdates;

    //Тут должна быть БД, которая тыкается через shceduler, если я всё правильно понимаю
    boolean addRequest(LinkUpdate req) {
        if (currentListOfUpdates.contains(req)) {
            return false;
        } else {
            currentListOfUpdates.add(req);
            return true;
        }
    }
}
