package edu.java.scrapper.database;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;
import static org.assertj.core.api.Assertions.within;

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
    @Rollback
    public void testThatGetEmptyDatabaseAndReturnedCorrectInsertsAndSelectsResults() {
        var jdbcTemplate = new JdbcTemplate(source);

        long givenChatId = 23;
        long givenLinkId = 45;
        OffsetDateTime time = OffsetDateTime.now();
        String givenUrl = "https://github.com/Skiftare/Java2024/pull/6";

        String queryToInsertInChatDatabase = "INSERT INTO chat (chat_id) VALUES (?)";
        String queryToInsertInLinkDatabase = "INSERT INTO link (url, created_at, last_update_at) VALUES (?, ?, ?)";

        jdbcTemplate.update(queryToInsertInChatDatabase, givenChatId);
        jdbcTemplate.update(queryToInsertInLinkDatabase, givenUrl, time, time);

        String queryToSelectFromChatDatabase = "SELECT chat_id FROM chat WHERE chat_id = ?";
        String queryToSelectFromLinkDatabase = "SELECT last_update_at FROM link WHERE url = ?";

        Integer realChatId = jdbcTemplate.queryForObject(queryToSelectFromChatDatabase, Integer.class, givenChatId);
        OffsetDateTime realTime = jdbcTemplate.queryForObject(queryToSelectFromLinkDatabase, OffsetDateTime.class, givenUrl);

        assertThat(realChatId).isEqualTo(givenChatId);
        assertThat(realTime).isCloseTo(time, within(1, ChronoUnit.SECONDS));
    }
}
