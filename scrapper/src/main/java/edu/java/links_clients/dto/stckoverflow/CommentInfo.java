package edu.java.links_clients.dto.stckoverflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentInfo {

    @JsonProperty("creation_date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private OffsetDateTime creationDate;
}
