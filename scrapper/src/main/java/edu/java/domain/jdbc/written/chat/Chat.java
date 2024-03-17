package edu.java.domain.jdbc.written.chat;

import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
public class Chat {
    private long id;
    private long tgChatId;
    private OffsetDateTime createdAt;
}
