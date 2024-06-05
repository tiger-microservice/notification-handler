package vn.tiger.notification.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.tiger.notification.configs.locale.Translator;
import vn.tiger.notification.dtos.response.ApiResponse;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Translator translator;

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<Object>> handlingRuntimeException(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.responseError(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    @ExceptionHandler(value = BusinessLogicException.class)
    ResponseEntity<ApiResponse<Object>> handlingBusinessLogicException(BusinessLogicException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.responseError(errorCode.getCode(), translator.toMessage(errorCode.getMessage())));
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Object>> handlingException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.responseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
    }
}
