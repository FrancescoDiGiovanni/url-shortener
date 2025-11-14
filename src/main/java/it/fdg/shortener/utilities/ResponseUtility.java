package it.fdg.shortener.utilities;

import it.fdg.shortener.exceptions.ShortenerApiHttpStatusException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Getter
@Setter
public class ResponseUtility {
    private static final String ERROR_CODE_MESSAGE_FORMAT = "{}: {}";

    public static <T> ResponseEntity<Response<T>> buildSuccessResponseEntity(String message, T payload, Logger log) {
        log.info(message);
        Response<T> response = new Response<>();
        return new ResponseEntity<>(response.buildResponse(true, message, payload), HttpStatus.OK);
    }

    public static <T> ResponseEntity<Response<T>> buildErrorResponseEntity(String code, HttpStatus httpStatus, String errorMessage, T payload, Logger log) {
        log.info(ERROR_CODE_MESSAGE_FORMAT, code, errorMessage);
        Response<T> response = new Response<>();
        return new ResponseEntity<>(response.buildErrorResponse(code, errorMessage, payload),
                httpStatus);
    }

    public static <T> ResponseEntity<Response<T>> buildErrorResponseEntityFromException(ShortenerApiHttpStatusException e, Logger log) {
        log.error(ERROR_CODE_MESSAGE_FORMAT, e.getCode(), e.getMessage(), e);
        Response<T> response = new Response<>();
        return new ResponseEntity<>(
                response.buildErrorResponse(e.getCode(), e.getMessage()),
                e.getHttpStatus()
        );
    }

    public static <T> boolean isResponseOk(ResponseEntity<Response<T>> responseEntity) {
        Response<T> responseEntityBody = responseEntity.getBody();
        return responseEntity.getStatusCode() == HttpStatus.OK && responseEntityBody != null && responseEntityBody.isSuccess();
    }
}
