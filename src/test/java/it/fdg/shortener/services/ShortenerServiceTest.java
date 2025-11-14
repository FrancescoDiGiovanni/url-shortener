package it.fdg.shortener.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fdg.shortener.dtos.UrlMappingDTO;
import it.fdg.shortener.entities.UrlMapping;
import it.fdg.shortener.repositories.UrlMappingRepository;
import it.fdg.shortener.services.impls.ShortenerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShortenerServiceTest {
    @Mock
    private UrlMappingRepository urlMappingRepository;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private ShortenerServiceImpl shortenerService;

    @Test
    void getOriginalUrlFromShortId_test001_givenShortId_Success() {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortId("10");
        urlMapping.setOriginalUrl("http://www.foo.com");
        urlMapping.setExpiration(LocalDateTime.now().plusDays(1));

        when(urlMappingRepository.findByShortIdAndExpirationAfter(eq("10"), any(LocalDateTime.class)))
                .thenReturn(Optional.of(urlMapping));

        when(objectMapper.convertValue(urlMapping, UrlMappingDTO.class))
                .thenReturn(UrlMappingDTO.builder().shortId("10").originalUrl("http://www.foo.com").build());

        UrlMappingDTO urlMappingDTO = shortenerService.getOriginalUrlFromShortId("10");
        assertNotNull(urlMappingDTO);
        assertEquals("http://www.foo.com", urlMappingDTO.getOriginalUrl());
    }

    @Test
    void getOriginalUrlFromShortId_test002_shortIdNotExisting_ReturnsNull() {
        when(urlMappingRepository.findByShortIdAndExpirationAfter(eq("10"), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        UrlMappingDTO urlMappingDTO = shortenerService.getOriginalUrlFromShortId("10");
        assertNull(urlMappingDTO);
    }

    @Test
    void getOriginalUrlFromShortId_test003_givenExpiredShortId_ReturnsNull() {
        when(urlMappingRepository.findByShortIdAndExpirationAfter(eq("10"), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        UrlMappingDTO response = shortenerService.getOriginalUrlFromShortId("10");

        assertNull(response);
        verify(urlMappingRepository, times(1))
                .findByShortIdAndExpirationAfter(eq("10"), any(LocalDateTime.class));
    }

    @Test
    void getOriginalUrlFromShortUrl_test001_givenShortUrl_Success() {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortId("10");
        urlMapping.setOriginalUrl("http://www.foo.com");
        urlMapping.setExpiration(LocalDateTime.now().plusDays(1));

        when(urlMappingRepository.findByShortIdAndExpirationAfter(eq("10"), any(LocalDateTime.class)))
                .thenReturn(Optional.of(urlMapping));

        when(objectMapper.convertValue(urlMapping, UrlMappingDTO.class))
                .thenReturn(UrlMappingDTO.builder().shortId("10").originalUrl("http://www.foo.com").build());

        UrlMappingDTO urlMappingDTO = shortenerService.getOriginalUrlFromShortUrl("http://localhost:8081/10");
        assertNotNull(urlMappingDTO);
        assertEquals("http://www.foo.com", urlMappingDTO.getOriginalUrl());
    }

    @Test
    void getOriginalUrlFromShortUrl_test0002_givenShortUrlNoShortId_ReturnsNull() {
        UrlMappingDTO urlMappingDTO = shortenerService.getOriginalUrlFromShortUrl("http://localhost:8081/");

        assertNull(urlMappingDTO);
    }
}
