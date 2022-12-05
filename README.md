# This is the Route Service.
### Task 1: Build a Restful API for finding the shortest path between countries.
**RoutingController.java** has action to Get a route. The controller accepts origin and destination countries as parameters.<br>
This routing controller provides the following endpoint:
- **GET** /api/v1/routing/{origin}/{destination} to get the shortest route if it is possible. http://localhost:8080/api/v1/routing/FRA/POL

### Task 2: Implement an algorithm to find the shortest route
1. The algorithm is implemented in DijkstraService.java
2. This implementation expects an unweighted graph (Vertices) - all edges are treated as one weighted.
3. It is implemented on a PriorityQueue.

### Task 3: Read Countries data from remote source
1. CountryQueryService.java fetches countries data and transforms it into our data structure ```List<Country>```
2. Cache is pre-warmed on app start-up and refreshed automatically.

### Tests
- **Integration Tests** are written with Spring-Test mockMvc. See RoutingControllerIntegrationTest
- **Unit Tests** are written with jUnit5 and Mockito. See RoutingServiceTest.java for details.

# Project development environment setup
1. Project requires JDK 17
2. Execute `mvn install` to build source code
3. Execute `mvn spring-boot:run` to start the project