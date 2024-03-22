package edu.java.bot.api.entities.requests;

import java.net.URI;
import org.jetbrains.annotations.NotNull;

public record AddLinkRequest(
    @NotNull
    URI link) {
}
