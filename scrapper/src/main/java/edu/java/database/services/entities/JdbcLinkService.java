package edu.java.database.services.entities;

import edu.java.database.dao.LinkDao;
import edu.java.database.dto.LinkDto;
import edu.java.database.services.interfaces.LinkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class JdbcLinkService implements LinkService {
    private final LinkDao linkDao;

    public JdbcLinkService(LinkDao linkDao) {
        this.linkDao = linkDao;
    }

    @Transactional
    @Override
    public LinkDto add(long tgChatId, URI url) {
        LinkDto link = new LinkDto(tgChatId, url.toString(), LocalDateTime.now());
        linkDao.add(link);
        return link;
    }

    @Transactional
    @Override
    public LinkDto remove(long tgChatId, URI url) {
        LinkDto link = new LinkDto(tgChatId, url.toString(), null);
        linkDao.remove(link);
        return link;
    }

    @Override
    public Collection<LinkDto> listAll(long tgChatId) {
        return linkDao.findAll().stream()
            .filter(link -> link.linkId().equals(tgChatId))
            .collect(Collectors.toList());
    }
}
