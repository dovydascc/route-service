package com.company.route.errorhandling;

/**
 * Generic Route Service Exception. It serves as a base class for all custom exceptions we define.
 * It is extended by more specific exceptions in the codebase
 */
public class RouteServiceException extends RuntimeException {

  public RouteServiceException(String message) {
    super(message);
  }
}
