package it.fdg.shortener.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fdg.shortener.dtos.UrlMappingDTO;
import it.fdg.shortener.entities.UrlMapping;
import it.fdg.shortener.exceptions.ShortenerApiHttpStatusException;
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

    @Test
    void createShortUrl_test001_givenOriginalUrl_Success() {
        String originalUrl = "http://foo.com";
        String generatedId = "10";

        when(urlMappingRepository.existsByShortId(anyString()))
                .thenReturn(false);

        UrlMapping newUrlMapping = new UrlMapping();
        newUrlMapping.setId(1);
        newUrlMapping.setShortId(generatedId);
        newUrlMapping.setOriginalUrl(originalUrl);
        newUrlMapping.setExpiration(LocalDateTime.now().plusDays(30));

        when(urlMappingRepository.save(any(UrlMapping.class)))
                .thenReturn(newUrlMapping);

        UrlMappingDTO dto = UrlMappingDTO.builder()
                .id(1)
                .shortId(generatedId)
                .originalUrl(originalUrl)
                .build();

        when(objectMapper.convertValue(any(), eq(UrlMappingDTO.class)))
                .thenReturn(dto);

        UrlMappingDTO result = shortenerService.createShortUrl(originalUrl);

        assertNotNull(result);
        assertEquals(generatedId, result.getShortId());
        assertEquals(originalUrl, result.getOriginalUrl());
    }

    @Test
    void createShortUrl_test002_givenOriginalUrlAndShortIdCollisionOccurred_Regenerate() {
        when(urlMappingRepository.existsByShortId(anyString()))
                .thenReturn(true)
                .thenReturn(false);

        UrlMapping savedUrl = new UrlMapping();
        savedUrl.setId(1);
        savedUrl.setShortId("10");
        savedUrl.setOriginalUrl("https://foo.com");

        when(urlMappingRepository.save(any())).thenReturn(savedUrl);

        when(objectMapper.convertValue(any(), eq(UrlMappingDTO.class)))
                .thenReturn(UrlMappingDTO.builder()
                        .id(1)
                        .shortId("10")
                        .originalUrl("https://foo.com")
                        .build());

        UrlMappingDTO result = shortenerService.createShortUrl("https://foo.com");

        assertNotNull(result);
        assertEquals("10", result.getShortId());

        verify(urlMappingRepository, times(2)).existsByShortId(anyString());
    }

    @Test
    void createShortUrl_test003_givenOriginalUrlNull_throwException() {
        assertThrows(ShortenerApiHttpStatusException.class,
                () -> shortenerService.createShortUrl(null));
    }

    @Test
    void createShortUrl_test004_originalUrlBlank_throwException() {
        assertThrows(ShortenerApiHttpStatusException.class,
                () -> shortenerService.createShortUrl(" "));
    }
}
