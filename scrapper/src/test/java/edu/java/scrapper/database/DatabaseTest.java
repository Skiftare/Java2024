package edu.java.scrapper.database;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DatabaseTest extends IntegrationTest {

    private static DataSource source;

    @BeforeAll
    public static void setUpDataSource() {
        source = DataSourceBuilder.create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build();
    }

    @Test
    public void testThatGetEmptyDatabaseAndReturnedCorrectInsertsAndSelectsResults() {
        var jdbcTemplate = new JdbcTemplate(source);

        long givenChatId = 123L;
        long givenLinkId = 1234L;
        String givenUrl = "https://github.com/zed-industries/zed";

        String queryToInsertInChatDatabase = "INSERT INTO chat (chat_id) VALUES (?)";
        String queryToInsertInLinkDatabase = "INSERT INTO link (link_id, url) VALUES (?, ?)";
        String queryToInsertInChainDatabase =
            "INSERT INTO link_chat_relation (id_of_chat, id_of_link) VALUES (?, ?)";

        jdbcTemplate.update(queryToInsertInChatDatabase, givenChatId);
        jdbcTemplate.update(queryToInsertInLinkDatabase, givenLinkId, givenUrl);
        jdbcTemplate.update(queryToInsertInChainDatabase, givenChatId, givenLinkId);

        String queryToSelectFromChatDatabase = "SELECT chat_id FROM chat WHERE chat_id = ?";
        String queryToSelectFromLinkDatabase = "SELECT url FROM link WHERE link_id = ?";
        String queryToSelectFromChainDatabase =
            "SELECT id_of_link FROM link_chat_relation WHERE id_of_chat = ?";

        Integer realChatId = jdbcTemplate.queryForObject(queryToSelectFromChatDatabase, Integer.class, givenChatId);
        String realUrl = jdbcTemplate.queryForObject(queryToSelectFromLinkDatabase, String.class, givenLinkId);
        Integer realChatAndLinkRelationChain =
            jdbcTemplate.queryForObject(queryToSelectFromChainDatabase, Integer.class, givenChatId);

        assertThat(realChatId).isEqualTo(givenChatId);
        assertThat(realUrl).isEqualTo(givenUrl);
        assertThat(realChatAndLinkRelationChain).isEqualTo(givenLinkId);
    }
}
