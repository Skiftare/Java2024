package edu.java.links_clients.stackoverflow;

import edu.java.links_clients.dto.stckoverflow.AnswerInfo;
import edu.java.links_clients.dto.stckoverflow.CommentInfo;
import java.util.List;
import java.util.Optional;

public interface StackOverflowClient {
    Optional<StackOverflowResponse> processQuestionUpdates(long questionId);

    List<AnswerInfo> getAnswerInfoByQuestion(Long question);

    List<CommentInfo> getCommentInfoByQuestion(Long question);
}
