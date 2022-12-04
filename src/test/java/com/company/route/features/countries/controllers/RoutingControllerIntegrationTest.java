package com.company.route.features.countries.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.company.route.IntegrationTest;
import com.company.route.features.countries.entities.Route;
import org.junit.jupiter.api.Test;

class RoutingControllerIntegrationTest extends IntegrationTest {

  @Test
  void givenCzechToItaly_whenGetRoute_thenFinds() throws Exception {
    // given
    var origin = "CZE";
    var destination = "ITA";
    // when
    var response = mockMvc.perform(
            get("/api/v1/routing/{origin}/{destination}", origin, destination).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    // then
    var route = objectMapper.readValue(response, Route.class);
    assertThat(route.countries()).containsExactly("CZE", "AUT", "ITA");
  }

  @Test
  void givenNorwayToFrance_whenGetRoute_thenFinds() throws Exception {
    // given
    var origin = "NOR";
    var destination = "FRA";
    // when
    var response = mockMvc.perform(
            get("/api/v1/routing/{origin}/{destination}", origin, destination).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    // then
    var route = objectMapper.readValue(response, Route.class);
    assertThat(route.countries()).containsExactly("NOR", "RUS", "POL", "DEU", "FRA");
  }
}