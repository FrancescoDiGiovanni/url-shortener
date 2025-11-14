package it.fdg.shortener.utilities;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;

@Getter
@Setter
public class Response<T> {
    private boolean success;
    private String message;
    private ErrorDetails error;
    private T payload;

    public Response<T> buildResponse(boolean success, String message, T payload) {
        this.setSuccess(success);
        this.setMessage(message);
        this.setPayload(payload);
        return this;
    }

    public Response<T> buildErrorResponse(String errorCode, String errorMessage) {
        this.setSuccess(false);
        this.setError(new ErrorDetails());
        this.error.setCode(errorCode);
        this.error.setMessage(errorMessage);
        this.error.setTraceId(MDC.get("traceId"));
        this.setPayload(null);
        return this;
    }

    public Response<T> buildErrorResponse(String errorCode, String errorMessage, T payload) {
        this.setSuccess(false);
        this.setError(new ErrorDetails());
        this.error.setCode(errorCode);
        this.error.setMessage(errorMessage);
        this.error.setTraceId(MDC.get("traceId"));
        this.setPayload(payload);
        return this;
    }
}
