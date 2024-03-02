package edu.java.bot.api.entities.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@EqualsAndHashCode(callSuper = true) @Data
@AllArgsConstructor
public record ListLinksResponse(
        List<LinkResponse> links,
        int size
){}
