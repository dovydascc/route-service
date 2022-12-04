package com.company.route.features.countries.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.company.route.errorhandling.NotFoundException;
import com.company.route.features.countries.integration.CountryService;
import com.company.route.features.dijkstra.DijkstraService;
import com.company.route.features.dijkstra.Edge;
import com.company.route.features.dijkstra.Vertex;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoutingServiceTest {

  @InjectMocks
  private RoutingService routingService;

  @Mock
  private CountryService countryService;

  @Mock
  private DijkstraService dijkstraService;

  @Test
  void givenCountriesGraph_whenGetRoute_thenCallsDijkstraService() {
    // given
    when(countryService.getCountriesGraph()).thenReturn(createCountriesGraph());
    var france = new Vertex("FRA");
    var germany = new Vertex("DEU");
    var poland = new Vertex("POL");
    var route = List.of(france, germany, poland);
    when(dijkstraService.getRoute(any(), any())).thenReturn(route);
    // when
    var result = routingService.getRoute("FRA", "POL");
    // then
    assertThat(result.countries()).hasSize(3);
    assertThat(result.countries()).containsExactly("FRA", "DEU", "POL");
  }

  @Test
  void givenNonExistingOrigin_whenGetRoute_thenThrows() {
    when(countryService.getCountriesGraph()).thenReturn(createCountriesGraph());
    assertThatThrownBy(() -> routingService.getRoute("AAA", "FRA")).isInstanceOf(NotFoundException.class);
  }

  @Test
  void givenNonExistingDestination_whenGetRoute_thenThrows() {
    when(countryService.getCountriesGraph()).thenReturn(createCountriesGraph());
    assertThatThrownBy(() -> routingService.getRoute("FRA", "ZZZ")).isInstanceOf(NotFoundException.class);
  }

  @Test
  void givenNoRoutePossible_whenGetRoute_thenThrows() {
    // given
    when(countryService.getCountriesGraph()).thenReturn(createCountriesGraph());
    var iceland = new Vertex("ISL");
    var route = List.of(iceland);
    when(dijkstraService.getRoute(any(), any())).thenReturn(route);
    // when
    when(countryService.getCountriesGraph()).thenReturn(createCountriesGraph());
    assertThatThrownBy(() -> routingService.getRoute("FRA", "ISL")).isInstanceOf(NotFoundException.class);
  }

  private Map<String, Vertex> createCountriesGraph() {
    var france = new Vertex("FRA");
    var germany = new Vertex("DEU");
    var poland = new Vertex("POL");
    var iceland = new Vertex("ISL");

    france.adjacencies(List.of(new Edge(germany)));
    germany.adjacencies(List.of(new Edge(france), new Edge(poland)));
    poland.adjacencies(List.of(new Edge(germany)));

    return Map.of(
        "FRA", france,
        "DEU", germany,
        "POL", poland,
        "ISL", iceland
    );
  }
}