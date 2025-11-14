package it.fdg.shortener.repositories;

import it.fdg.shortener.entities.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Integer> {
}
