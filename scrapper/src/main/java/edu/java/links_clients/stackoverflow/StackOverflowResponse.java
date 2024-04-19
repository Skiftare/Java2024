package edu.java.links_clients.stackoverflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackOverflowResponse(
    Owner owner,
    @JsonProperty("last_activity_date")
    OffsetDateTime lastActivityDate,
    @JsonProperty("answer_id")
    long answerId,
    @JsonProperty("question_id")
    long questionId,
    @JsonProperty("answer_count")
    int answersCount
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Owner(
        @JsonProperty("display_name")
        String displayName
    ) {
    }
}
