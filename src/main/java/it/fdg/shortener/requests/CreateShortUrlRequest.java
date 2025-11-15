package it.fdg.shortener.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShortUrlRequest {
    private String originalUrl;
}
