package edu.java.domain.jpa.services;

import edu.java.data.request.AddLinkRequest;
import edu.java.data.request.RemoveLinkRequest;
import edu.java.data.response.LinkResponse;
import edu.java.data.response.ListLinksResponse;
import edu.java.database.services.interfaces.LinkService;
import edu.java.domain.jpa.dao.Chat;
import edu.java.domain.jpa.dao.Link;
import edu.java.domain.jpa.written.JpaChatRepository;
import edu.java.domain.jpa.written.JpaLinkRepository;
import edu.java.exceptions.entities.LinkAlreadyExistException;
import edu.java.exceptions.entities.LinkNotFoundException;
import edu.java.exceptions.entities.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
public class JpaLinkService implements LinkService {

    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;
    @Override
    public ListLinksResponse listAll(long tgChatId) {
        Chat chat = getChatByTgChatId(tgChatId);

        List<LinkResponse> linkResponses = chat.getLinks()
            .stream()
            .map(row -> new LinkResponse(row.getId(), row.getUrl()))
            .toList();

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    public LinkResponse add(long tgChatId, AddLinkRequest linkRequest) {
        URI url = linkRequest.link();
        Chat chat = getChatByTgChatId(tgChatId);


        Link actualLink;
        if (linkRepository.findLinkByUrl(String.valueOf(url)).isEmpty()) {
            OffsetDateTime nowTime = OffsetDateTime.now();
            Link createLink = createLink(String.valueOf(url), nowTime, nowTime);
            linkRepository.save(createLink);
            actualLink = linkRepository.findLinkByUrl(String.valueOf(url)).get();
        } else {
            actualLink = linkRepository.findLinkByUrl(String.valueOf(url)).get();
            for (Link link : chat.getLinks()) {
                if (link.getUrl().equals(actualLink.getUrl())) {
                    throw new LinkAlreadyExistException(
                        generateExceptionMessageForChatId(tgChatId)
                            + ", "
                            + generateExceptionMessageForLinkUrl(actualLink.getUrl())
                    );
                }
            }
        }

        chat.addLink(actualLink);

        return new LinkResponse(actualLink.getId(), actualLink.getUrl());
    }

    @Override
    public LinkResponse remove(long tgChatId, RemoveLinkRequest linkRequest) {
        Chat chat = getChatByTgChatId(tgChatId);
        URI url = linkRequest.link();
        Link actualLink = getLinkByUrl(String.valueOf(url));
        chat.removeLink(actualLink);

        return new LinkResponse(actualLink.getId(), actualLink.getUrl());
    }

    private Chat getChatByTgChatId(long tgChatId) {
        return chatRepository.findChatWithLinkByTgChatId(tgChatId)
            .orElseThrow(
                () -> new UserNotFoundException(generateExceptionMessageForChatId(tgChatId))
            );
    }

    private Link getLinkByUrl(String url) {
        return linkRepository.findLinkWithChatByUrl(url)
            .orElseThrow(
                () -> new LinkNotFoundException(generateExceptionMessageForLinkUrl(url))
            );
    }

    private Link createLink(
        String url,
        OffsetDateTime createdAt,
        OffsetDateTime lastUpdateAt
    ) {
        Link link = new Link();
        link.setUrl(url);
        link.setCreatedAt(createdAt);
        link.setLastUpdateAt(lastUpdateAt);

        return link;
    }
}
