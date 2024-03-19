package edu.java.data.request;

import java.net.URI;
import java.util.List;

public record LinkUpdate(
    URI url,
    String description,
    List<Long> tgChatIds
) {
}
