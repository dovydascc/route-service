package com.company.route.features.countries.controllers;

import com.company.route.features.countries.entities.Route;
import com.company.route.features.countries.services.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for planning a route through Country Border crossings.
 */
@Validated
@RestController
@RequestMapping("/api/v1/routing")
@RequiredArgsConstructor
public class RoutingController {

  private final RoutingService routingService;

  /**
   * API to get a route from origin country to destination country.
   * It returns a single route only if the journey is possible.
   * Routing is as efficient as possible
   * @param origin The origin country abbreviation
   * @param destination The destination country abbreviation
   * @return The planned route
   */
  @GetMapping("/{origin}/{destination}")
  Route getRoute(@PathVariable String origin, @PathVariable String destination) {
    return routingService.getRoute(origin, destination);
  }
}
