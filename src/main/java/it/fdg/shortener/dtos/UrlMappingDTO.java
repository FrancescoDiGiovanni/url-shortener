package it.fdg.shortener.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class UrlMappingDTO {
    private Integer id;
    private String shortId;
    private String originalUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiration;
    private Integer hitCount;
    private LocalDateTime lastAccessed;
}
