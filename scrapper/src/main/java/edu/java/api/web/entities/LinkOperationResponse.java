package edu.java.api.web.entities;

import java.net.URI;

public record LinkOperationResponse(
    long chatId,
    URI url
) {
}
