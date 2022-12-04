package com.company.route.errorhandling;

/**
 * Validation Exception is thrown when user input is invalid or business logic is in invalid state.
 */
public class ValidationException extends RouteServiceException {

  public ValidationException(String message) {
    super(message);
  }
}
