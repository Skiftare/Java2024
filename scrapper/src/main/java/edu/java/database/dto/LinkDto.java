package edu.java.database.dto;

import java.time.LocalDateTime;

public record LinkDto(Long linkId, String url, LocalDateTime createdAt) {
}

