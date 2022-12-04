package com.company.route.errorhandling;

import java.time.ZonedDateTime;
import lombok.Value;

/**
 * ErrorResponse is returned from REST APIs when ErrorHandlingAdvice catches an error. Frontend applications depend on
 * this data structure and may use data field to get information about validation errors
 */
@Value
public class ErrorResponse {

  ZonedDateTime timestamp;
  String error;
  String message;
  Object data;
}
