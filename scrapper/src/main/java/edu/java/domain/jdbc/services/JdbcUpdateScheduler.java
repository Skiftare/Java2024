package edu.java.domain.jdbc.services;

import edu.java.api.BotServiceForWebClient;
import edu.java.data.request.LinkUpdateRequest;
import edu.java.domain.jdbc.dao.JdbcLinkChatRelationDao;
import edu.java.domain.jdbc.dao.JdbcLinkDao;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkRelation;
import edu.java.domain.jdbc.written.chat_link_relation.ChatLinkWithTgChat;
import edu.java.domain.jdbc.written.link.Link;
import edu.java.links_clients.LinkHandler;
import edu.java.links_clients.dto.github.GithubActions;
import edu.java.links_clients.dto.stckoverflow.AnswerInfo;
import edu.java.links_clients.dto.stckoverflow.CommentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true", matchIfMissing = true)
@Slf4j
public class JdbcUpdateScheduler {
    private static final Duration NEED_TO_CHECK = Duration.ofSeconds(30);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy");
    private static final String GIT_HEAD = "В репозитории %s, %d новых изменений:\n";
    private static final String GIT_ABOUT = "В ветку %s были внесены изменения (тип: %s, время %s)\n";
    private static final String SOF_HEAD = "В вопросе %s, появились новые ответы/кометарии:\n";
    private static final String SOF_ANSWER = "Новый ответ (время %s)\n";
    private static final String SOF_COMMENT = "Новый комментарий (время %s)\n";
    private final JdbcLinkDao linkDao;
    private final JdbcLinkChatRelationDao chatLinkDao;
    private final BotServiceForWebClient botService;
    private final LinkHandler webResourceHandler;

    public JdbcUpdateScheduler(
        JdbcLinkDao linkDao,
        JdbcLinkChatRelationDao chatLinkDao,
        BotServiceForWebClient botService,
        LinkHandler webResourceHandler
    ) {
        this.linkDao = linkDao;
        this.chatLinkDao = chatLinkDao;
        this.botService = botService;
        this.webResourceHandler = webResourceHandler;
    }

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        OffsetDateTime now = OffsetDateTime.now();
        List<Link> links = linkDao.getByLastUpdate(now.minus(NEED_TO_CHECK));
        for (Link link: links) {
            if (webResourceHandler.isGitHubUrl(link.getUrl())) {
                gitHubProcess(link, now);
            } else {
                stackOverflowProcess(link, now);
            }
            linkDao.updateLastUpdateAtById(link.getDataLinkId(), now);
        }
    }

    private void gitHubProcess(Link link, OffsetDateTime now) {
        List<GithubActions> actionsInfo = webResourceHandler.getActionsGitHubInfoByUrl(link.getUrl());
        if (link.getLastUpdateAt().isBefore(actionsInfo.getFirst().getPushedAt())) {
            StringBuilder description =
                new StringBuilder(format(GIT_HEAD, link.getUrl(), actionsInfo.size()));
            actionsInfo.stream()
                .filter(repI -> repI.getPushedAt().isAfter(link.getLastUpdateAt()))
                .map(repI -> format(
                    GIT_ABOUT,
                    repI.getRef(),
                    repI.getActivityType(),
                    repI.getPushedAt().format(FORMATTER)
                ))
                .forEach(description::append);
            updateTablesAndSendMsg(link, now, description.toString());
        }
    }

    private void stackOverflowProcess(Link link, OffsetDateTime now) {
        List<AnswerInfo> newAnswers = webResourceHandler.getAnswersStackOverflowByUrl(link.getUrl())
            .stream()
            .filter(answer -> answer.getLastActivityDate().isAfter(link.getLastUpdateAt()))
            .toList();
        List<CommentInfo> newComments = webResourceHandler.getCommentsStackOverflowByUrl(link.getUrl())
            .stream()
            .filter(comment -> comment.getCreationDate().isAfter(link.getLastUpdateAt()))
            .toList();

        if (!newAnswers.isEmpty() || !newComments.isEmpty()) {
            StringBuilder description =
                new StringBuilder(format(SOF_HEAD, link.getUrl()));
            newAnswers.forEach(answer -> description.append(format(
                SOF_ANSWER,
                answer.getLastEditDate() == null ? answer.getLastActivityDate().format(FORMATTER)
                    : answer.getLastEditDate().format(FORMATTER)
            )));
            newComments.forEach(comment -> description.append(format(
                SOF_COMMENT,
                comment.getCreationDate().format(FORMATTER)
            )));
            updateTablesAndSendMsg(link, now, description.toString());
        }
    }

    private void updateTablesAndSendMsg(Link link, OffsetDateTime newUpdateTime, String description) {
        long linkId = link.getDataLinkId();
        linkDao.updateLastUpdateAtById(linkId, newUpdateTime);
        List<Long> chatIdsToSendMsg = chatLinkDao.getByLinkId(linkId)
            .stream()
            .map(ChatLinkRelation::getDataChatId)
            .toList();
        botService.sendUpdate(linkId, link.getUrl(), description, chatIdsToSendMsg);
    }
}

