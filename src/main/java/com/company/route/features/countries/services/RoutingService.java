package com.company.route.features.countries.services;

import com.company.route.errorhandling.NotFoundException;
import com.company.route.features.countries.entities.Route;
import com.company.route.features.countries.integration.CountryService;
import com.company.route.features.dijkstra.DijkstraService;
import com.company.route.features.dijkstra.Vertex;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service finds the shortest path between countries.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoutingService {

  private final CountryService countryService;
  private final DijkstraService dijkstraService;

  /**
   * Get a route between Origin country and Destination country.
   *
   * @param origin An abbreviated country name (e.g. FRA for France)
   * @param destination An abbreviated country name (e.g. POL for Poland)
   * @return Shortest route between countries
   */
  public Route getRoute(String origin, String destination) {
    var countriesGraph = countryService.getCountriesGraph();
    var originCountry = getCountryVertex(origin, countriesGraph);
    var destinationCountry = getCountryVertex(destination, countriesGraph);
    var verticesRoute = dijkstraService.getRoute(originCountry, destinationCountry);
    throwIfRouteNotPossible(origin, destination, verticesRoute);
    return new Route(verticesRoute.stream().map(Vertex::name).toList());
  }

  private Vertex getCountryVertex(String origin, Map<String, Vertex> countriesGraph) {
    var vertex = countriesGraph.get(origin);
    if (vertex == null) {
      throw new NotFoundException(String.format("Country named:%s was not found. Please check the input", origin));
    }
    return vertex;
  }

  private void throwIfRouteNotPossible(String origin, String destination, List<Vertex> vertexRoute) {
    if (vertexRoute.size() == 1 && vertexRoute.get(0).name().equals(destination)) {
      throw new NotFoundException(String.format("There is no route from %s to %s over land.", origin, destination));
    }
  }
}
