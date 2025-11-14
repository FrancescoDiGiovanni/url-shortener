package it.fdg.shortener.repositories;

import it.fdg.shortener.entities.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Integer> {
    Optional<UrlMapping> findByShortIdAndExpirationAfter(String shortId, LocalDateTime expirationAfter);
}
