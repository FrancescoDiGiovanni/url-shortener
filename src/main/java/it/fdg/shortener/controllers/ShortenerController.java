package it.fdg.shortener.controllers;

import it.fdg.shortener.dtos.UrlMappingDTO;
import it.fdg.shortener.exceptions.ShortenerApiHttpStatusException;
import it.fdg.shortener.services.ShortenerService;
import it.fdg.shortener.utilities.Response;
import it.fdg.shortener.utilities.ResponseUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "shortener")
public class ShortenerController {
    @Autowired
    private ShortenerService shortenerService;

    @GetMapping(value = "/{shortId}", produces = "application/json")
    public ResponseEntity<Response<UrlMappingDTO>> getOriginalUrl(@PathVariable String shortId) throws ShortenerApiHttpStatusException {
        UrlMappingDTO urlMappingDTO = shortenerService.getOriginalUrlFromShortId(shortId);
        if ( urlMappingDTO == null )
            return ResponseUtility.buildErrorResponseEntity("SHORT0002", HttpStatus.NOT_FOUND, "No original url found with this short id.", null, log);

        String okMessage = "Shortened url found successfully.";
        return ResponseUtility.buildSuccessResponseEntity(okMessage, urlMappingDTO, log);
    }
}
