package edu.java.domain.jpa.services;

import edu.java.api.BotServiceForWebClient;
import edu.java.database.services.interfaces.LinkUpdater;
import edu.java.domain.jpa.dao.Chat;
import edu.java.domain.jpa.dao.Link;
import edu.java.domain.jpa.written.JpaLinkRepository;
import edu.java.links_clients.LinkHandler;
import edu.java.links_clients.dto.github.GithubActions;
import edu.java.links_clients.dto.stckoverflow.AnswerInfo;
import edu.java.links_clients.dto.stckoverflow.CommentInfo;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import static java.lang.String.format;

@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true", matchIfMissing = true)
@Slf4j
public class JpaLinkUpdaterScheduler implements LinkUpdater {
    private static final Duration NEED_TO_CHECK = Duration.ofSeconds(30);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yyyy");

    private static final String GIT_HEAD = "В репозитории %s, %d новых изменений:\n";
    private static final String GIT_ABOUT = "В ветку %s были внесены изменения (тип: %s, время %s)\n";
    private static final String SOF_HEAD = "В вопросе %s, появились новые ответы/кометарии:\n";
    private static final String SOF_ANSWER = "Новый ответ (время %s)\n";
    private static final String SOF_COMMENT = "Новый комментарий (время %s)\n";

    private final JpaLinkRepository linkRepository;
    private final BotServiceForWebClient botService;
    private final LinkHandler webResourceHandler;
    private final Logger logger = LoggerFactory.getLogger(JpaLinkUpdaterScheduler.class);


    public JpaLinkUpdaterScheduler(
        JpaLinkRepository jpaLinkRepository,
        BotServiceForWebClient botService,
        LinkHandler webResourceHandler
    ) {
        this.linkRepository = jpaLinkRepository;
        this.botService = botService;
        this.webResourceHandler = webResourceHandler;
    }


    @Override
    @Scheduled(fixedDelayString = "#{@app.scheduler.interval}")
    @Transactional
    public void checkForUpdates() {
        linkRepository.deleteUnnecessary();
        logger.info("Data optimizing completed");
        OffsetDateTime now = OffsetDateTime.now();
        List<Link> links = linkRepository.findLinksByLastUpdateAtBefore(now.minus(NEED_TO_CHECK));
        for (Link link : links) {
            logger.info("Updating link with ID: {}", link.getId());
            if (webResourceHandler.isGitHubUrl(link.getUrl())) {
                logger.info("GitHub link: {}", link.getUrl());
                gitHubProcess(link, now);
            } else {
                stackOverflowProcess(link, now);
            }
            linkRepository.updateLastUpdateAtById(link.getId(), now);
        }
    }

    private void gitHubProcess(Link link, OffsetDateTime now) {

        List<GithubActions> actionsInfo = webResourceHandler.getActionsGitHubInfoByUrl(link.getUrl());
        logger.info("actionsInfo size: {}", actionsInfo.size());
        logger.info("link.getLastUpdateAt(): {}", link.getLastUpdateAt());
        if (link.getLastUpdateAt().isBefore(actionsInfo.getFirst().getPushedAt())) {
            logger.info("link.getLastUpdateAt().isBefore queried pushedAt");
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
        long linkId = link.getId();
        linkRepository.updateLastUpdateAtById(linkId, newUpdateTime);
        List<Long> chatIdsToSendMsg = link.getChats()
            .stream()
            .map(Chat::getTgChatId)
            .toList();
        botService.sendUpdate(linkId, link.getUrl(), description, chatIdsToSendMsg);
    }
}
