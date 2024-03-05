package edu.java.bot.api.entities.requests;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class RemoveLinkRequest {
    @NotNull
    private URI link;
}
