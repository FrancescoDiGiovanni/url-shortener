package it.fdg.shortener.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fdg.shortener.requests.CreateShortUrlRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ShortenerControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void create_then_get_success() throws Exception {
        CreateShortUrlRequest req = new CreateShortUrlRequest();
        req.setOriginalUrl("http://foo.com");

        var result = mockMvc.perform(post("/shortener/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.shortId").exists())
                .andReturn();

        String shortId =
                mapper.readTree(result.getResponse().getContentAsString())
                        .get("payload").get("shortId").asText();

        mockMvc.perform(get("/shortener/" + shortId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.originalUrl").value("http://foo.com"));
    }

    @Test
    void create_invalidUrl_returns400() throws Exception {
        CreateShortUrlRequest req = new CreateShortUrlRequest();
        req.setOriginalUrl(" ");

        mockMvc.perform(post("/shortener/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("SHORT0001"));
    }

    @Test
    void get_notExisting_returns404() throws Exception {
        mockMvc.perform(get("/shortener/not_foundaaa"))
                .andExpect(status().isNotFound());
    }
}
