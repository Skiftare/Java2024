package edu.java.bot.api.link_updater.request;

import java.net.URI;
import java.util.ArrayList;

public record RequestToUpdate(URI url, ArrayList<Long> userIdsInterestedInTheLink) {
    //Ну а вдруг все захотят наблюдать, например, за https://github.com/zed-industries/zed?tab=readme-ov-file
    //Когда он уже выйдет на винду/линукс
}
