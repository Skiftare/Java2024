package edu.java.links_clients.stackoverflow;

import java.util.Optional;

public interface StackOverflowClient {
    Optional<StackOverflowResponse> processQuestionUpdates(long questionId);
}
