package com.task.absensi.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle API business logic exceptions - HTTP 501
     * Messages should be human-readable
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Handle validation errors - HTTP 400 Bad Request
     * Messages can be technical/system messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        body.put("message", "Validasi input gagal: " + errors);
        body.put("errors", ex.getBindingResult().getFieldErrors());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle generic exceptions - HTTP 500 Internal Server Error
     * Messages can be technical/system messages
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Terjadi kesalahan internal server: " + ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
