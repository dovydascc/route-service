package com.company.route.features.countries.integration;

import com.company.route.features.dijkstra.Edge;
import com.company.route.features.dijkstra.Vertex;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service constructs Countries Graph that is suitable for Dijkstra algorithm.
 */
@Service
@RequiredArgsConstructor
public class CountryService {

  private final CountryQueryService countryQueryService;

  /**
   * Get a Map of countries (Vertexes).
   * Each Country (Vertex) will have references to Neighbor Countries (adjacent Vertices).
   *
   * @return A Map with Country abbreviation as a key, and Country-Vertex as a value.
   */
  public Map<String, Vertex> getCountriesGraph() {
    var countries = countryQueryService.fetchCountries();

    var verticesMap = countries.parallelStream()
        .collect(Collectors.toMap(Country::abbreviation, country -> new Vertex(country.abbreviation())));

    countries.forEach(country -> {
      var vertex = verticesMap.get(country.abbreviation());
      var borders = country.borders()
          .parallelStream()
          .map(border -> new Edge(verticesMap.get(border)))
          .toList();
      vertex.adjacencies(borders);
    });
    return verticesMap;
  }
}
