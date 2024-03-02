package edu.java.bot.api.link_updater.mapper;

import edu.java.bot.api.link_updater.request.RequestToUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/update")
@RequiredArgsConstructor
public class UpdateMapper {
    private static final String SUCCESS_LINK_UPDATED = "Ссылка добавлена в очередь на парсинг";
    private static final String MANY_SAME_LINKS_IN_UPDATE_LIST =
        "Эта ссылка уже есть в очереди на обработку. Всему своё время.";
    private final UpdateManager updateManager;

    @PostMapping
    public String updateLinkState(RequestToUpdate request) {
        return updateManager.addRequest(request) ?
            SUCCESS_LINK_UPDATED : MANY_SAME_LINKS_IN_UPDATE_LIST;
    }
}
