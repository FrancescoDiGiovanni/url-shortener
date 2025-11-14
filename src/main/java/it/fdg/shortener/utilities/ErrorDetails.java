package it.fdg.shortener.utilities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorDetails {
    private String code;
    private String message;
    private String traceId;
}
