package edu.java.api.entities.responses;

import java.util.List;

public record ListLinksResponse(
    List<LinkResponse> links,
    int size
) {}
