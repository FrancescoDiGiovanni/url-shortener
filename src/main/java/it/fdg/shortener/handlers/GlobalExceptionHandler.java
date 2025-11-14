package it.fdg.shortener.handlers;

import it.fdg.shortener.exceptions.ShortenerApiHttpStatusException;
import it.fdg.shortener.utilities.Response;
import it.fdg.shortener.utilities.ResponseUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ShortenerApiHttpStatusException.class)
    public <T> ResponseEntity<Response<T>> handleServiceExceptions(
            ShortenerApiHttpStatusException ex) {
        return ResponseUtility.buildErrorResponseEntityFromException(ex, log);
    }
}
