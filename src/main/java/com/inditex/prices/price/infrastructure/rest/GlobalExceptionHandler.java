package com.inditex.prices.price.infrastructure.rest;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import com.inditex.prices.price.domain.exception.PriceNotFoundException;
import com.inditex.prices.price.infrastructure.rest.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(PriceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(
      final PriceNotFoundException exception, final HttpServletRequest request) {
    return this.build(HttpStatus.NOT_FOUND, exception.getMessage(), request);
  }

  @ExceptionHandler({
    MethodArgumentTypeMismatchException.class,
    MissingServletRequestParameterException.class,
    HandlerMethodValidationException.class,
    ConstraintViolationException.class,
    InvalidPriceException.class
  })
  public ResponseEntity<ErrorResponse> handleBadRequest(
      final Exception exception, final HttpServletRequest request) {
    return this.build(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpectedError(
      final Exception exception, final HttpServletRequest request) {
    log.error(
        "Unexpected error handling {} {}", request.getMethod(), request.getRequestURI(), exception);
    return this.build(
        HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error has occurred.", request);
  }

  private ResponseEntity<ErrorResponse> build(
      final HttpStatus status, final String message, final HttpServletRequest request) {
    final ErrorResponse errorResponse =
        new ErrorResponse(
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI(),
            LocalDateTime.now());
    return ResponseEntity.status(status).body(errorResponse);
  }
}
