package edu.java.domain.jdbc.written.chat_link_relation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatLinkRelation {
    private long dataChatId;
    private long dataLinkId;

    public static ChatLinkRelation makeChatLinkRelation(long chatId, long linkId) {
        ChatLinkRelation chatLinkRelation = new ChatLinkRelation();
        chatLinkRelation.setDataChatId(chatId);
        chatLinkRelation.setDataLinkId(linkId);
        return chatLinkRelation;
    }
}
