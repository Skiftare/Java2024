package edu.java.links_clients.dto.stckoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.util.List;

@Getter
public class AnswerItems {
    @JsonProperty("items")
    private List<AnswerInfo> answerInfo;
}
