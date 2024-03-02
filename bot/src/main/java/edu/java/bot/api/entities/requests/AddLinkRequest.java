package edu.java.bot.api.entities.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import java.net.URI;

@Data
@AllArgsConstructor
public class AddLinkRequest {

    @NotNull
    private URI link;

}
