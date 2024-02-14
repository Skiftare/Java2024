package edu.java.bot.processor;

import lombok.Getter;

@Getter public enum DialogState {
    TRACK_URI("trackURI"),
    UNTRACK_URI("untrackURI"),
    DEFAULT_SESSION("defaultSession");

    private String value;

    private DialogState(String value) {
        this.value = value;
    }

}
