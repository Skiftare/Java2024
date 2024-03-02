package edu.java.bot.api.entities.responses;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {}
