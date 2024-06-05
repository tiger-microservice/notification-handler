package vn.tiger.notification.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    INVALID_KEY(400, "MSG00001", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(404, "MSG00002", HttpStatus.NOT_FOUND),
    BEAN_NOT_DEFINED(404, "MSG00003", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(401, "MSG00006", HttpStatus.UNAUTHORIZED),
    UNCATEGORIZED_EXCEPTION(500, "MSG00007", HttpStatus.INTERNAL_SERVER_ERROR),
;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
