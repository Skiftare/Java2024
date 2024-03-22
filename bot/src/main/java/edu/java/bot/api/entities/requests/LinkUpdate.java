package edu.java.bot.api.entities.requests;

import java.net.URI;
import java.util.List;

public record LinkUpdate(
    Long id,
    URI url,
    String description,
    List<Long> tgChatIds
) {
}
