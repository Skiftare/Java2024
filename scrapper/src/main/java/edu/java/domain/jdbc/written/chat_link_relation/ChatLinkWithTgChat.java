package edu.java.domain.jdbc.written.chat_link_relation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatLinkWithTgChat {
    private long dataChatId;
    private long dataLinkId;
    private long tgChatId;
}
