package edu.java.database.services.interfaces;


import edu.java.database.dto.LinkDto;
import java.net.URI;
import java.util.Collection;

public interface LinkService {
    LinkDto add(long tgChatId, URI url);
    LinkDto remove(long tgChatId, URI url);
    Collection<LinkDto> listAll();
}
