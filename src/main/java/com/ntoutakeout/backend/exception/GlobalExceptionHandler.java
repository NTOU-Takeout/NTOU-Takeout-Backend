package com.ntoutakeout.backend.exception;

import com.ntoutakeout.backend.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(400, exception.getMessage());
        return ResponseEntity.status(400).body(apiResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchElementException(NoSuchElementException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(404, exception.getMessage());
        return ResponseEntity.status(404).body(apiResponse);
    }
}