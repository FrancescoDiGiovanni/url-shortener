package it.fdg.shortener.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fdg.shortener.dtos.UrlMappingDTO;
import it.fdg.shortener.exceptions.ShortenerApiHttpStatusException;
import it.fdg.shortener.handlers.GlobalExceptionHandler;
import it.fdg.shortener.requests.CreateShortUrlRequest;
import it.fdg.shortener.services.ShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ShortenerControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ShortenerService shortenerService;

    @InjectMocks
    private ShortenerController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getOriginalUrl_test001_givenShortIdFound_Returns200() throws Exception {
        UrlMappingDTO dto = UrlMappingDTO.builder()
                .shortId("10")
                .originalUrl("http://foo.com")
                .build();

        when(shortenerService.getOriginalUrlFromShortId("10"))
                .thenReturn(dto);

        mockMvc.perform(get("/shortener/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.originalUrl").value("http://foo.com"));
    }

    @Test
    void getOriginalUrl_test002_givenShortIdNotFound_Returns404() throws Exception {
        when(shortenerService.getOriginalUrlFromShortId("10a"))
                .thenReturn(null);

        mockMvc.perform(get("/shortener/10a"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("SHORT0002"))
                .andExpect(jsonPath("$.error.message")
                        .value("No original url found with this short id."));
    }

    @Test
    void createShortId_test001_success() throws Exception {
        UrlMappingDTO dto = UrlMappingDTO.builder()
                .id(1)
                .shortId("10")
                .originalUrl("http://foo.com")
                .build();

        when(shortenerService.createShortUrl("http://foo.com"))
                .thenReturn(dto);

        CreateShortUrlRequest request = new CreateShortUrlRequest();
        request.setOriginalUrl("http://foo.com");

        mockMvc.perform(post("/shortener/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.shortId").value("10"))
                .andExpect(jsonPath("$.payload.originalUrl").value("http://foo.com"));
    }

    @Test
    void createShortId_test002_invalidUrl_returnsError() throws Exception {
        when(shortenerService.createShortUrl(anyString()))
                .thenThrow(new ShortenerApiHttpStatusException(
                        "SHORT0001",
                        "No original url provided during creation request.",
                        HttpStatus.BAD_REQUEST
                ));

        CreateShortUrlRequest request = new CreateShortUrlRequest();
        request.setOriginalUrl(" ");

        mockMvc.perform(post("/shortener/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("SHORT0001"));
    }

    @Test
    void createShortId_test003_missingBody_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/shortener/")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}
