package edu.java.domain.jdbc.written.chat_link_relation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatLinkWithUrl {
    private long chatId;
    private long linkId;
    private String url;
}
