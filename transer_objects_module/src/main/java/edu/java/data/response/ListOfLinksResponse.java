package edu.java.data.response;

import java.util.List;

public record ListOfLinksResponse(Long chatId, List<LinkResponse> resultList
) {
}
