package edu.java.links_clients.dto.stckoverflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.List;


@Getter
public class CommentItems {
    @JsonProperty("items")
    private List<CommentInfo> commentInfo;
}
