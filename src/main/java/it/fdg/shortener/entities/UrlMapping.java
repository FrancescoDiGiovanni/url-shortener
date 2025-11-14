package it.fdg.shortener.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_mappings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlMapping {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "short_id", nullable = false)
    private String shortId;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expiration")
    private LocalDateTime expiration;

    @Column(name = "hit_count")
    private Integer hitCount;

    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;
}
