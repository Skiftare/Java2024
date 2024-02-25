package edu.java.stackoverflow;

import java.util.Optional;

public interface StackOverflowClient {
    Optional<StackOverflowResponse> processQuestionUpdates(long questionId);
}
