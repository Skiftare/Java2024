package edu.java.api.entities.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record LinkUpdateRequest(
    @NotNull
    @Positive(message = "INVALID_ID")
    Long id,
    @NotNull URI url,
    @NotEmpty String description,
    @NotEmpty List<Long> tgChatIds
) {
}
