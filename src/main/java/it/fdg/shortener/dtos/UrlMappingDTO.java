package it.fdg.shortener.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlMappingDTO {
    private Integer id;
    private String shortId;
    private String originalUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiration;
    private Integer hitCount;
    private LocalDateTime lastAccessed;
}
