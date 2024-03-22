package edu.java.api.web.entities;

import edu.java.api.entities.responses.LinkResponse;
import java.util.List;

public record ListOfLinksResponse(Long chatId, List<LinkResponse> resultList
) {
}
