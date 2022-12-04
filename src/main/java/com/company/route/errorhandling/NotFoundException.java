package com.company.route.errorhandling;

/**
 * Not Found Exception is thrown when we can not find data requested. Usually this means data does not exist in the
 * database, or data was removed.
 */
public class NotFoundException extends RouteServiceException {

  public NotFoundException(String message) {
    super(message);
  }
}
