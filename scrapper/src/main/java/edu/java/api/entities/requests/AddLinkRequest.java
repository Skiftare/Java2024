package edu.java.api.entities.requests;

import org.jetbrains.annotations.NotNull;
import java.net.URI;

public record AddLinkRequest(
    @NotNull URI link
) {}
