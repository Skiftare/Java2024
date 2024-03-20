package edu.java.domain.jdbc.written.chat;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Chat {
    private long tgChatId;
    private OffsetDateTime createdAt;

    public static Chat makeChat(long id, OffsetDateTime time) {
        Chat chat = new Chat();
        chat.setTgChatId(id);
        chat.setCreatedAt(time);
        return chat;
    }
}
