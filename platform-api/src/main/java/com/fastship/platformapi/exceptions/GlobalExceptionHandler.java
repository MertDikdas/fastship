package com.fastship.platformapi.exceptions;

import com.fastship.platformapi.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(DeployableServiceNotFoundException.class)
    public ResponseEntity<ApiError> handleDeployableServiceNotFound(
            DeployableServiceNotFoundException e,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "DEPLOYABLE_SERVICE_NOT_FOUND",
                e.getMessage(),
                request.getRequestURI().toLowerCase(Locale.ROOT),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ApiError> handleProjectNotFound(
            ProjectNotFoundException e,
            HttpServletRequest request
    ){
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "PROJECT_NOT_FOUND",
                e.getMessage(),
                request.getRequestURI().toLowerCase(Locale.ROOT),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String message = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage()
                )
                .orElse("Validation failed");

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                message,
                request.getRequestURI().toLowerCase(Locale.ROOT),
                Instant.now()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

}
