package com.example.gsmetrics.prometheus.controller.advice;

import com.example.gsmetrics.prometheus.dto.GenericErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericErrorDto> handleException(final Exception ex, WebRequest request) {
        log.error("Exception - Error during execution of: {}", request, ex);
        GenericErrorDto genericErrorDto = new GenericErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity.internalServerError().body(genericErrorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GenericErrorDto> handleError(final IllegalArgumentException ex, WebRequest request) {
        log.error("IllegalArgumentException - Error during execution of: {}", request, ex);
        GenericErrorDto genericErrorDto = new GenericErrorDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(genericErrorDto);
    }

}
