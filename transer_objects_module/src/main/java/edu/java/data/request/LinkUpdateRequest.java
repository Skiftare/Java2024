package edu.java.data.request;

import java.net.URI;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
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
