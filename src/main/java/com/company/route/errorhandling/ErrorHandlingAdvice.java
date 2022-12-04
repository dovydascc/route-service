package com.company.route.errorhandling;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import jakarta.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global error handler for all REST APIs. It is implemented to catch all exceptions.
 */
@Slf4j
@ResponseBody
@ControllerAdvice
class ErrorHandlingAdvice {

  /**
   * Method Argument not valid exception is raised when Spring can not match HTTP request input with data types declared
   * inside Controller
   *
   * @param e the exception
   *
   * @return a uniform error response
   */
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler
  ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
    log.info("MethodArgumentNotValidException: {}", e.getMessage());

    var errors = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fe -> String.format("%s: %s", fe.getField(), fe.getDefaultMessage()))
        .collect(toList());

    return getErrorResponse(e.getClass().getSimpleName(), "Invalid request, see data", errors);
  }

  /**
   * Data Integrity constraint violations are thrown by database constraint validations. Violations are created by
   * invalid request body or query params.
   *
   * @param e the exception
   * @return a uniform error response
   */
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler
  ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    log.info("DataIntegrityViolationException: {}", e.getMessage());

    if (!(e.getCause() instanceof ConstraintViolationException)) {
      return handleThrowable(e);
    }

    var ex = (ConstraintViolationException) e.getCause();
    var errors = ex.getConstraintViolations()
        .stream()
        .map(cv -> String.format("%s: %s", cv.getMessage(), cv.getInvalidValue()))
        .collect(toList());

    return getErrorResponse(e.getClass().getSimpleName(), "Invalid request params, see data", errors);
  }

  /**
   * Entity Not Found exception is raised when this service doesn't have requested data in the database
   *
   * @param e the exception
   * @return a uniform error response
   */
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler
  ErrorResponse handleNotFoundException(NotFoundException e) {
    log.info("NotFoundException: {}", e.getMessage());
    return getErrorResponse(e.getClass().getSimpleName(), "Entity does not exist", e.getMessage());
  }

  /**
   * Custom Validation Exception is raised when user's input is invalid or we can not proceed requested action on
   * existing data.
   *
   * @param e the exception
   * @return a uniform error response
   */
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler
  ErrorResponse handleValidationException(ValidationException e) {
    log.info("ValidationException: {}", e.getMessage());
    return getErrorResponse(e.getClass().getSimpleName(), "Validation error", e.getMessage());
  }

  /**
   * Generic error handler, for all errors unhandled above. This will return HTTP 500 Internal Server Error.
   *
   * @param e the exception
   * @return a uniform error response
   */
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler
  ErrorResponse handleThrowable(Throwable e) {
    log.error("InternalServerError: ", e);
    return getErrorResponse(e.getClass().getSimpleName(), "Internal server error, please contact support", null);
  }

  private ErrorResponse getErrorResponse(String error, String message, Object data) {
    return new ErrorResponse(ZonedDateTime.now(), error, message, data);
  }
}
