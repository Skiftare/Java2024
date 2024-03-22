package edu.java.database.dto;

import java.time.LocalDateTime;

public record ChatDto(Long chatId, LocalDateTime createdAt) {
}
