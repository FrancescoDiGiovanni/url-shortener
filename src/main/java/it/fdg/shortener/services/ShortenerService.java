package it.fdg.shortener.services;

import it.fdg.shortener.dtos.UrlMappingDTO;

public interface ShortenerService {
    UrlMappingDTO getOriginalUrlFromShortId(String shortId);
    UrlMappingDTO getOriginalUrlFromShortUrl(String shortUrl);
}
