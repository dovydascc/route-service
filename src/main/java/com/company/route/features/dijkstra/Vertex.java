package com.company.route.features.dijkstra;

import java.util.List;

/**
 * A Vertex for a graph, for Dijkstra algorithm.
 */
public class Vertex implements Comparable<Vertex> {

  private final String name;
  private List<Edge> adjacencies = List.of();
  private int minDistance = Integer.MAX_VALUE;
  private Vertex previous;

  public Vertex(String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }

  public List<Edge> adjacencies() {
    return adjacencies;
  }

  public void adjacencies(List<Edge> adjacencies) {
    this.adjacencies = adjacencies;
  }

  public int minDistance() {
    return minDistance;
  }

  public void minDistance(int minDistance) {
    this.minDistance = minDistance;
  }

  public Vertex previous() {
    return previous;
  }

  public void previous(Vertex previous) {
    this.previous = previous;
  }

  public String toString() {
    return name;
  }

  public int compareTo(Vertex other) {
    return Integer.compare(minDistance, other.minDistance);
  }
}
