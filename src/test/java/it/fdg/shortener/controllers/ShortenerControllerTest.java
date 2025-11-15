package it.fdg.shortener.controllers;

import it.fdg.shortener.dtos.UrlMappingDTO;
import it.fdg.shortener.services.ShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ShortenerControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ShortenerService shortenerService;

    @InjectMocks
    private ShortenerController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
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
}
