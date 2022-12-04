package com.company.route.features.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import org.springframework.stereotype.Service;

/**
 * Service executes Dijkstra algorithm.
 * This implementation expects an unweighted graph (Vertices) - all edges are treated as one weighted.
 * It is implemented on a PriorityQueue.
 */
@Service
public class DijkstraService {

  /**
   * Get the shortest route from origin to destination, based on the Dijkstra algorithm.
   * @param origin The origin Vertex, where a search starts
   * @param destination The destination Vertex to finish the search
   * @return Shortest route (a list of vertices)
   */
  public List<Vertex> getRoute(Vertex origin, Vertex destination) {
    computePaths(origin);
    return getShortestPathTo(destination);
  }

  private void computePaths(Vertex source) {
    source.minDistance(0);
    PriorityQueue<Vertex> vertexQueue = new PriorityQueue<>();
    vertexQueue.add(source);

    while (!vertexQueue.isEmpty()) {
      Vertex u = vertexQueue.poll();

      // Visit each edge exiting u
      for (Edge e : u.adjacencies()) {
        Vertex v = e.target();
        int distanceThroughU = u.minDistance() + 1;
        if (distanceThroughU < v.minDistance()) {
          vertexQueue.remove(v);

          v.minDistance(distanceThroughU);
          v.previous(u);
          vertexQueue.add(v);
        }
      }
    }
  }

  private List<Vertex> getShortestPathTo(Vertex target) {
    List<Vertex> path = new ArrayList<>();
    for (Vertex vertex = target; vertex != null; vertex = vertex.previous()) {
      path.add(vertex);
    }

    Collections.reverse(path);
    return path;
  }
}