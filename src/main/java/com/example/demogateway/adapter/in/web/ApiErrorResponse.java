package com.example.demogateway.adapter.in.web;

import static com.example.demogateway.common.StringUtils.camelToSnake;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.util.StringUtils.hasText;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Responsible for building KCP v3 compliant error response objects.
 * <p>
 * Nested classes adapt exceptions to the API error objects.
 */

public final class ApiErrorResponse {

  private final List<ApiError<?>> errors;

  ApiErrorResponse(final ApiError<?> error) {
    this.errors = Collections.singletonList(error);
  }

  public List<ApiError<?>> getErrors() {
    return errors;
  }

  //===========================================
  // Default throwable mapping to API errors
  //===========================================

  @JsonInclude(JsonInclude.Include.NON_NULL)
  // Needed to suppress missing path or reasons from output
  public static class ApiError<T extends Throwable> {

    private final T error;
    private final ErrorDateTime datetime = new ErrorDateTime();
    private final List<RootCause> rootCauses;

    ApiError(final T error) {
      this(error, null);
    }

    ApiError(final T error, final List<RootCause> rootCauses) {
      this.error = error;
      this.rootCauses = rootCauses;
    }

    public String getPath() {
      return null;
    }

    public List<RootCause> getRootCauses() {
      return rootCauses;
    }

    public String getCode() {
      return camelToSnake(error.getClass().getSimpleName());
    }

    public String getReason() {
      return hasText(error.getMessage()) ? error.getMessage() : "Unknown reason.";
    }

    public ErrorDateTime getDatetime() {
      return datetime;
    }

    protected T getError() {
      return error;
    }

    public static class ErrorDateTime {

      private final String timezone = ZoneId.systemDefault().getId();
      private final String value = OffsetDateTime.now(ZoneId.of("UTC")).toString();

      public String getValue() {
        return value;
      }

      public String getTimezone() {
        return timezone;
      }
    }

    //===========================================================================================
    // Some exceptions have validation details that are mapped to root causes such as
    // constraint violation and object error collections.
    //===========================================================================================

    interface RootCause {

      String getCode();

      String getReason();
    }

    //===========================================================================================
    // ObjectErrors exist within several exceptions and is defined here.
    //===========================================================================================


    static final class ObjectErrorRootCause implements ApiError.RootCause {

      private final ObjectError violation;

      ObjectErrorRootCause(final ObjectError violation) {
        this.violation = violation;
      }

      public String getCode() {
        return "VALIDATION_ERROR";
      }

      public String getReason() {
        return violation.getDefaultMessage();
      }
    }
  }

  //===========================================================================================
  // Map ApplicationException to API error and nested ConstraintViolations to API root causes
  //===========================================================================================

  public static final class ConstraintViolationError extends
      ApiError<ConstraintViolationException> {

    ConstraintViolationError(final ConstraintViolationException error) {
      super(error, error.getConstraintViolations().stream()
          .map(ConstraintViolationRootCause::new)
          .collect(toList()));
    }

    @Override
    public String getCode() {
      return BAD_REQUEST.name();
    }

    @Override
    public String getReason() {
      return "Constraint violation(s) for input data";
    }

    static final class ConstraintViolationRootCause implements RootCause {

      private final ConstraintViolation<?> violation;

      ConstraintViolationRootCause(final ConstraintViolation<?> violation) {
        this.violation = violation;
      }

      public String getCode() {
        return "VALIDATION_ERROR";
      }

      public String getReason() {
        return violation.getMessage();
      }
    }
  }

  //===========================================================================================
  // Map BindException to API error and nested ObjectErrors to API root causes
  //===========================================================================================

  public static final class BindError extends ApiError<BindException> {

    BindError(final BindException error) {
      super(error, error.getAllErrors().stream()
          .map(ObjectErrorRootCause::new)
          .collect(toList()));
    }

    @Override
    public String getCode() {
      return BAD_REQUEST.name();
    }

    @Override
    public String getReason() {
      return "Constraint violation(s) for input data";
    }
  }

  //=============================================================================================
  // Map MethodArgumentNotValidExceptions to API error and nested ObjectErrors to API root causes
  //==============================================================================================

  public static final class MethodArgumentNotValidError extends
      ApiError<MethodArgumentNotValidException> {

    MethodArgumentNotValidError(final MethodArgumentNotValidException error) {
      super(error, error.getBindingResult().getAllErrors().stream()
          .map(ObjectErrorRootCause::new)
          .collect(toList()));
    }

    @Override
    public String getCode() {
      return BAD_REQUEST.name();
    }

    @Override
    public String getReason() {
      return "Constraint violation(s) for input data";
    }
  }
}
