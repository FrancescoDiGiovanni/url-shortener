package it.fdg.shortener.services.impls;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fdg.shortener.dtos.UrlMappingDTO;
import it.fdg.shortener.entities.UrlMapping;
import it.fdg.shortener.repositories.UrlMappingRepository;
import it.fdg.shortener.services.ShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShortenerServiceImpl implements ShortenerService {
    @Autowired
    private UrlMappingRepository urlMappingRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public UrlMappingDTO getOriginalUrlFromShortId(String shortId) {
        Optional<UrlMapping> urlMapping = urlMappingRepository.findByShortIdAndExpirationAfter(shortId, LocalDateTime.now());
        return urlMapping.map((mapping) -> objectMapper.convertValue(mapping, UrlMappingDTO.class))
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public UrlMappingDTO getOriginalUrlFromShortUrl(String shortUrl) {
        URI uri = URI.create(shortUrl);
        String path = uri.getPath();
        if (path.startsWith("/"))
            path = path.substring(1);

        return getOriginalUrlFromShortId(path);
    }
}
