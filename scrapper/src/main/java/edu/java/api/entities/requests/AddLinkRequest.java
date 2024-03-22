package edu.java.api.entities.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URI;

@Schema(description = "AddLinkRequest", required = true)
public record AddLinkRequest(URI link) {

}
