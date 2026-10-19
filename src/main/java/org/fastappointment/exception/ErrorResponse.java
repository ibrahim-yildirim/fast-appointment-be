package org.fastappointment.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final List<FieldErrorResponse> fieldErrors;

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path,
                         List<FieldErrorResponse> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public List<FieldErrorResponse> getFieldErrors() {
        return fieldErrors;
    }
}
