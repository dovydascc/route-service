package com.company.route.errorhandling;

import java.util.Objects;
import lombok.Getter;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mock controller for testing Exception Handling in our application
 */
@RestController
@RequestMapping("/api/v1/test/")
class MockExceptionController {

  @GetMapping("/ok")
  String ok() {
    return "ok";
  }

  @GetMapping("/argument-not-valid")
  void argumentNotValid() throws MethodArgumentNotValidException {
    var someMethod = ReflectionUtils.findMethod(MockExceptionController.class, "argumentNotValid");
    var errors = new BeanPropertyBindingResult(new Driver(), "driver");
    errors.rejectValue("firstName", "UNIQUE", "Please enter unique name");
    errors.rejectValue("lastName", "MINIMUM", "Please enter at least 3 characters");

    throw new MethodArgumentNotValidException(new MethodParameter(Objects.requireNonNull(someMethod), -1), errors);
  }

  @GetMapping("/not-found")
  void notFound() {
    throw new NotFoundException("testing");
  }

  @GetMapping("/validation-exception")
  void validationException() {
    throw new ValidationException("testing");
  }

  @GetMapping("/some-throwable")
  void someThrowable() {
    throw new RouteServiceException("testing");
  }

  static class Driver {

    @Getter
    private String firstName;
    @Getter
    private String lastName;
  }
}