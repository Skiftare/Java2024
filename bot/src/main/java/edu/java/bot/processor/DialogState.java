package edu.java.bot.processor;

import lombok.Getter;

@Getter
public enum DialogState {
    TRACK_URI("trackURI"),
    UNTRACK_URI("untrackURI"),
    DEFAULT_SESSION("defaultSession"),
    NOT_REGISTERED("not registered");

    private final String value;

    DialogState(String value) {
        this.value = value;
    }

}
