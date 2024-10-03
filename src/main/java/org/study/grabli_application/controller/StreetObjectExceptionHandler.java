package org.study.grabli_application.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.study.grabli_application.dto.ErrorResponse;
import org.study.grabli_application.exceptions.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class StreetObjectExceptionHandler {
    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, fieldError ->
                        Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Некорректное значение")));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(SizeException.class)
    public ResponseEntity<ErrorResponse> handleException(SizeException e, HttpServletRequest request) {
        String message = "Размер файла не должен превышать " + maxFileSize.toMegabytes() + "МБ";
        return newResponse(e, request, HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleException(AccessDeniedException e, HttpServletRequest request) {
        String message = "Доступ запрещён";
        return newResponse(e, request, HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(EntityCreationException.class)
    public ResponseEntity<ErrorResponse> handleException(EntityCreationException e, HttpServletRequest request) {
        return newResponse(e, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(EntityNotFoundException e, HttpServletRequest request) {
        return newResponse(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(ImageNotFoundException e, HttpServletRequest request) {
        return newResponse(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ImageStorageException.class)
    public ResponseEntity<ErrorResponse> handleException(ImageStorageException e, HttpServletRequest request) {
        return newResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ErrorResponse> handleException(ImageUploadException e, HttpServletRequest request) {
        return newResponse(e, request, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> newResponse(
            Exception e, HttpServletRequest request, HttpStatus status
    ) {
        String message = e.getMessage();
        return newResponse(e, request, status, message);
    }

    private ResponseEntity<ErrorResponse> newResponse(
            Exception e, HttpServletRequest request, HttpStatus status, String message
    ) {
        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            log.error(message, e);
        } else {
            log.info(message);
        }

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(LocalDateTime.now(), requestString(request), statusString(status), message));
    }

    private String requestString(HttpServletRequest request) {
        return request.getMethod() + " " + request.getRequestURI();
    }

    private String statusString(HttpStatus status) {
        return status.value() + " " + status.getReasonPhrase();
    }
}
