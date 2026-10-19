package org.fastappointment.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        String message = translate(ex.getMessageKey(), ex.getArgs());
        return buildResponse(HttpStatus.NOT_FOUND, message, request, null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
        String message = translate(ex.getMessageKey(), ex.getArgs());
        return buildResponse(HttpStatus.CONFLICT, message, request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                         HttpServletRequest request) {
        List<FieldErrorResponse> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();
        String message = translate(ErrorMessageKeys.VALIDATION_FAILED);
        return buildResponse(HttpStatus.BAD_REQUEST, message, request, fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex,
                                                             HttpServletRequest request) {
        String message = translate(ErrorMessageKeys.MALFORMED_REQUEST);
        return buildResponse(HttpStatus.BAD_REQUEST, message, request, null);
    }

    // Args are turned into plain strings so ids aren't formatted as numbers ("1,234" / "1.234").
    private String translate(String messageKey, Object... args) {
        Object[] textArgs = Arrays.stream(args).map(String::valueOf).toArray();
        return messageSource.getMessage(messageKey, textArgs, LocaleContextHolder.getLocale());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request,
                                                        List<FieldErrorResponse> fieldErrors) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.status(status).body(body);
    }
}
