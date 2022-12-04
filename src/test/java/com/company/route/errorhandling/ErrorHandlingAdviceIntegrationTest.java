package com.company.route.errorhandling;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.company.route.IntegrationTest;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class ErrorHandlingAdviceIntegrationTest extends IntegrationTest {

  @Test
  void givenValidRequest_whenControllerCalled_thenNoException() throws Exception {
    mockMvc.perform(get("/api/v1/test/ok")).andExpect(status().isOk());
  }

  @Test
  void givenArgumentNotValid_whenControllerCalled_thenHttp400() throws Exception {
    var response = mockMvc.perform(get("/api/v1/test/argument-not-valid"))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString();
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);

    assertThat(errorResponse.getError()).isEqualTo("MethodArgumentNotValidException");
    assertThat(errorResponse.getMessage()).isEqualTo("Invalid request, see data");
    assertThat(errorResponse.getData()).asList()
        .containsExactly("firstName: Please enter unique name", "lastName: Please enter at least 3 characters");
    assertThat(errorResponse.getTimestamp()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.MINUTES));
  }


  @Test
  void givenNotFoundException_whenControllerCalled_thenHttp400() throws Exception {
    var response = mockMvc.perform(get("/api/v1/test/not-found"))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString();
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);

    assertThat(errorResponse.getError()).isEqualTo("NotFoundException");
    assertThat(errorResponse.getMessage()).isEqualTo("Entity does not exist");
    assertThat(errorResponse.getData()).asString().isEqualTo("testing");
    assertThat(errorResponse.getTimestamp()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.MINUTES));
  }

  @Test
  void givenValidationException_whenControllerCalled_thenHttp400() throws Exception {
    var response = mockMvc.perform(get("/api/v1/test/validation-exception"))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString();
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);

    assertThat(errorResponse.getError()).isEqualTo("ValidationException");
    assertThat(errorResponse.getMessage()).isEqualTo("Validation error");
    assertThat(errorResponse.getData()).asString().isEqualTo("testing");
    assertThat(errorResponse.getTimestamp()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.MINUTES));
  }

  @Test
  void givenInternalServerError_whenControllerCalled_thenHttp500() throws Exception {
    var response = mockMvc.perform(get("/api/v1/test/some-throwable"))
        .andExpect(status().isInternalServerError())
        .andReturn()
        .getResponse()
        .getContentAsString();
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);

    assertThat(errorResponse.getError()).isEqualTo("RouteServiceException");
    assertThat(errorResponse.getMessage()).isEqualTo("Internal server error, please contact support");
    assertThat(errorResponse.getData()).isNull();
    assertThat(errorResponse.getTimestamp()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.MINUTES));
  }
}