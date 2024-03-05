package edu.java.scrapper.api;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ScrapperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final static String TEST_LINK = """
        {
            "link":"https://example.com"
        }
        """;

    @Test
    @DisplayName("Chat: POST; Correct request")
    public void postChatCorrect() throws Exception {
        long id = 1;
        mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/{id}", id))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Chat: POST; Incorrect request")
    public void postChatIncorrect() throws Exception {
        long id = -1;
        mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/{id}", id))
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Chat: DELETE; Correct request")
    public void deleteChatCorrect() throws Exception {
        long id = 1;
        mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/{id}", id))
            .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/tg-chat/{id}", id))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Chat: DELETE; Incorrect request")
    public void deleteChatIncorrect() throws Exception {
        long id = -1;
        mockMvc.perform(MockMvcRequestBuilders.delete("/tg-chat/{id}", id))
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Links: GET; Correct request")
    public void getLinksCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/links")
                .header("Tg-Chat-Id", "1"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Links: GET; Incorrect request")
    public void getLinksIncorrect() throws Exception {
        long id = -1;
        mockMvc.perform(MockMvcRequestBuilders.get("/links")
                .header("Tg-Chat-Id", "-1"))
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Link: POST; Correct request")
    public void postLinksCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/links")
                .header("Tg-Chat-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_LINK))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Link: POST; Incorrect request")
    public void postLinksIncorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/links")
                .header("Tg-Chat-Id", "-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_LINK))
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("Link: DELETE; Correct request")
    public void deleteLinksCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/links")
                .header("Tg-Chat-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_LINK))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Link: DELETE; Incorrect request")
    public void deleteLinksIncorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/links")
                .header("Tg-Chat-Id", "-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_LINK))
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
    }

}
