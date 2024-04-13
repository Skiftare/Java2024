package edu.java.links_clients.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.links_clients.github.GitHubResponse;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubActions {
    private Long id;
    private String ref;
    @JsonProperty("timestamp")
    private OffsetDateTime pushedAt;
    @JsonProperty("activity_type")
    private String activityType;
    private GitHubResponse.Actor actor;

    public void setRef(String ref) {
        String[] strings = ref.split("/");
        this.ref = strings[strings.length - 1];
    }
}
