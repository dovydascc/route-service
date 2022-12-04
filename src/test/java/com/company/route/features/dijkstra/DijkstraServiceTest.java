package com.company.route.features.dijkstra;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DijkstraServiceTest {

  private final DijkstraService dijkstraService = new DijkstraService();

  private final Vertex france = new Vertex("FRA");
  private final Vertex germany = new Vertex("DEU");
  private final Vertex poland = new Vertex("POL");
  private final Vertex belgium = new Vertex("BEL");
  private final Vertex italy = new Vertex("ITA");
  private final Vertex luxembourg = new Vertex("LUX");
  private final Vertex spain = new Vertex("ESP");
  private final Vertex switzerland = new Vertex("CHE");
  private final Vertex austria = new Vertex("AUT");
  private final Vertex denmark = new Vertex("DNK");
  private final Vertex ukraine = new Vertex("UKR");

  @BeforeEach
  void setUp() {
    france.adjacencies(List.of(
        new Edge(belgium),
        new Edge(germany),
        new Edge(italy),
        new Edge(luxembourg),
        new Edge(spain),
        new Edge(switzerland)
    ));
    germany.adjacencies(List.of(
        new Edge(austria),
        new Edge(belgium),
        new Edge(switzerland),
        new Edge(denmark),
        new Edge(france),
        new Edge(poland)
    ));
    poland.adjacencies(List.of(
        new Edge(ukraine),
        new Edge(germany)
    ));
  }

  @Test
  void givenFranceToPoland_whenGetRoute_thenFindsShortestPath() {
    var route = dijkstraService.getRoute(france, poland);
    assertThat(route).containsExactly(france, germany, poland);
  }

  @Test
  void givenGermanyToUkraine_whenGetRoute_thenFindsShortestPath() {
    var route = dijkstraService.getRoute(germany, ukraine);
    assertThat(route).containsExactly(germany, poland, ukraine);
  }

  @Test
  void givenGermanyToGermany_whenGetRoute_thenFindsShortestPath() {
    var route = dijkstraService.getRoute(germany, germany);
    assertThat(route).containsExactly(germany);
  }
}