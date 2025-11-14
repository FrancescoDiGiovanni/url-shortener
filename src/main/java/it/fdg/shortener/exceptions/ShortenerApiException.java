package it.fdg.shortener.exceptions;

import lombok.Getter;

@Getter
public class ShortenerApiException extends RuntimeException{
    private final String code;

    public ShortenerApiException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ShortenerApiException(String code, String message) {
        super(message);
        this.code = code;
    }
}
