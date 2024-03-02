package edu.java.api.entities.requests;

import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    @NotNull Long id,
    @NotNull URI url,
    @NotEmpty String description,
    @NotEmpty List<Long> tgChatIds
) {}
