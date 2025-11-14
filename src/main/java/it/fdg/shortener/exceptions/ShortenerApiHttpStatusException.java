package it.fdg.shortener.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ShortenerApiHttpStatusException extends ShortenerApiException {
    private final HttpStatus httpStatus;
    private final String responseMessage;

    public ShortenerApiHttpStatusException(String code, String message, Throwable cause, HttpStatus httpStatus) {
        super(code, message, cause);
        this.httpStatus = httpStatus;
        this.responseMessage = "";

    }

    public ShortenerApiHttpStatusException(String code, String message, HttpStatus httpStatus) {
        super(code, message);
        this.httpStatus = httpStatus;
        this.responseMessage = "";
    }

}
