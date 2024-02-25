package edu.java.stackoverflow;

import java.util.Optional;

public interface StackOverflowClient {
    Optional<StackOverflowResponse> fetchQuestionUpdates(long questionId);
}
