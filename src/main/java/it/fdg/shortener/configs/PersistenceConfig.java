package it.fdg.shortener.configs;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("it.fdg.shortener.repositories")
@EntityScan("it.fdg.shortener.entities")
public class PersistenceConfig {
}
