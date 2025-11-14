package it.fdg.shortener.services.impls;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.seruco.encoding.base62.Base62;
import it.fdg.shortener.dtos.UrlMappingDTO;
import it.fdg.shortener.entities.UrlMapping;
import it.fdg.shortener.exceptions.ShortenerApiHttpStatusException;
import it.fdg.shortener.repositories.UrlMappingRepository;
import it.fdg.shortener.services.ShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class ShortenerServiceImpl implements ShortenerService {
    @Autowired
    private UrlMappingRepository urlMappingRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private final static Base62 BASE62 = Base62.createInstance();

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

    @Override
    @Transactional
    public UrlMappingDTO createShortUrl(String originalUrl) {
        if ( originalUrl == null  || originalUrl.isBlank() )
            throw new ShortenerApiHttpStatusException("SHORT0001", "No original url provided during creation request.", HttpStatus.BAD_REQUEST);

        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://"))
            originalUrl = "https://" + originalUrl;

        String shortId = generateUniqueShortId();

        UrlMapping mapping = new UrlMapping();
        mapping.setShortId(shortId);
        mapping.setOriginalUrl(originalUrl);
        mapping.setCreatedAt(LocalDateTime.now());
        mapping.setExpiration(LocalDateTime.now().plusDays(100));
        mapping.setHitCount(0);
        mapping.setLastAccessed(null);
        UrlMapping saved = urlMappingRepository.save(mapping);

        return objectMapper.convertValue(saved, UrlMappingDTO.class);
    }

    @Override
    public String generateUniqueShortId() {
        String shortId;

        do {
            long randomValue = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
            byte[] longBytes = ByteBuffer.allocate(Long.BYTES).putLong(randomValue).array();

            String encoded = new String(BASE62.encode(longBytes));
            shortId = encoded.substring(0,7);

        } while (urlMappingRepository.existsByShortId(shortId));

        return shortId;
    }
}
