package com.company.route.features.countries.integration;

import com.company.route.errorhandling.RouteServiceException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service fetches country data from GitHub source countries file.
 *
 * Note, this service does not use RestTemplate, because
 * GitHub returns source data as text/plain; and we can't apply application/json
 */
@Service
@RequiredArgsConstructor
public class CountryQueryService {

  private final Environment env;
  private final ObjectMapper objectMapper;

  /**
   * Fetch data from GitHub source countries file (configurable in application.yml)
   * This service Cache the data in our EhCache storage.
   * The data is fetched on app startup, and then a Scheduled job refreshes the cache every day.
   *
   * @return A list of countries with their borders
   */
  @Cacheable("countries")
  @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
  public List<Country> fetchCountries() {
    // we do not use RestTemplate, because this endpoint doesn't provide json, it produce text/plain; instead.
    try {
      var url = new URL(Objects.requireNonNull(env.getProperty("routeService.integration.countriesEndpoint")));
      return objectMapper.readValue(url, new TypeReference<>() {
      });
    } catch (IOException e) {
      throw new RouteServiceException("There was an error while calling countries endpoint" + e.getMessage());
    }
  }
}
