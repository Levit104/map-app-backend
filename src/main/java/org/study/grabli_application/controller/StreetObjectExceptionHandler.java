package org.study.grabli_application.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.study.grabli_application.exceptions.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class StreetObjectExceptionHandler {
    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;

    @ExceptionHandler(SizeException.class)
    public ResponseEntity<ErrorResponse> handleException(SizeException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return newResponse(
                request,
                HttpStatus.BAD_REQUEST,
                "Размер файла не должен превышать " + maxFileSize.toMegabytes() + "МБ"
        );
    }

    private ResponseEntity<ErrorResponse> newResponse(HttpServletRequest request, HttpStatus status, String message) {
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
