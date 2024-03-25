package edu.java.links_clients.dto.stckoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class AnswerItems {
    @JsonProperty("items")
    private List<AnswerInfo> answerInfo;
}
