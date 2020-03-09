package com.example.demogateway.adapter.in.web;

import static com.example.demogateway.common.LoggerUtils.logWithContext;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Converts and returns any thrown application exceptions into a KCP formatted error response.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

  /**
   * Exception handler methods
   */

  @ExceptionHandler({
      IllegalArgumentException.class,
      HttpMessageNotReadableException.class,
      HttpMediaTypeNotSupportedException.class
  })
  public ResponseEntity<?> handleBadRequestExceptions(
      final Throwable e,
      final HttpServletRequest request
  ) {
    logWithContext(() -> log.warn(e.getMessage()), request);
    return ResponseEntity.badRequest()
        .body(new ApiErrorResponse(new ApiErrorResponse.ApiError<>(e)));
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<?> handleBindException(
      final BindException e,
      final HttpServletRequest request
  ) {
    logWithContext(() -> log.warn(e.getMessage()), request);
    return ResponseEntity.badRequest()
        .body(new ApiErrorResponse(new ApiErrorResponse.BindError(e)));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleConstraintViolationException(
      final ConstraintViolationException e,
      final HttpServletRequest request
  ) {
    logWithContext(() -> log.warn(e.getMessage()), request);
    return ResponseEntity.badRequest()
        .body(new ApiErrorResponse(new ApiErrorResponse.ConstraintViolationError(e)));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException e,
      final HttpServletRequest request
  ) {
    logWithContext(() -> log.warn(e.getMessage()), request);
    return ResponseEntity.badRequest()
        .body(new ApiErrorResponse(new ApiErrorResponse.MethodArgumentNotValidError(e)));
  }

  // Handle unsupported HTTP verbs as 405
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<?> handleMethodNotAllowedException(
      final HttpRequestMethodNotSupportedException e,
      final HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
  }

  // Everything else is HTTP status code 500
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<?> handleUnexpectedExceptions(
      final Throwable e,
      final HttpServletRequest request
  ) {
    logWithContext(() -> log.error(e.getMessage()), request);
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(new ApiErrorResponse(new ApiErrorResponse.ApiError<>(e)));
  }
}
