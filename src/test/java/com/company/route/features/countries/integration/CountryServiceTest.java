package com.company.route.features.countries.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.company.route.features.dijkstra.Edge;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

  @InjectMocks
  private CountryService countryService;

  @Mock
  private CountryQueryService countryQueryService;

  @Test
  void givenCountriesList_whenGetCountriesGraph_thenReferencesBordersAsVertices() {
    // given
    when(countryQueryService.fetchCountries()).thenReturn(createCountries());
    // when
    var countriesMap = countryService.getCountriesGraph();
    // then
    assertThat(countriesMap).hasSize(3);
    var france = countriesMap.get("FRA");
    var germany = countriesMap.get("DEU");
    var poland = countriesMap.get("POL");
    assertThat(france.adjacencies()).contains(new Edge(germany));
    assertThat(germany.adjacencies()).contains(new Edge(poland));
    assertThat(poland.adjacencies()).contains(new Edge(germany));
  }

  @Test
  void givenNoCountries_whenGetCountriesGraph_thenEmpty() {
    // when
    var countriesMap = countryService.getCountriesGraph();
    // then
    assertThat(countriesMap).isEmpty();
  }

  @Test
  void givenIslandCountry_whenGetCountriesGraph_thenOneCountryWithoutReferences() {
    // given
    when(countryQueryService.fetchCountries()).thenReturn(Collections.singletonList(new Country("ISL", List.of())));
    // when
    var countriesMap = countryService.getCountriesGraph();
    // then
    assertThat(countriesMap).hasSize(1);
    var france = countriesMap.get("ISL");
    assertThat(france.adjacencies()).isEmpty();
  }

  private List<Country> createCountries() {
    return List.of(
        new Country("FRA", List.of("AND", "BEL", "DEU", "ITA", "LUX", "MCO", "ESP", "CHE")),
        new Country("DEU", List.of("AUT", "BEL", "CZE", "DNK", "FRA", "LUX", "NLD", "POL", "CHE")),
        new Country("POL", List.of("BLR", "CZE", "DEU", "LTU", "RUS", "SVK", "UKR"))
    );
  }
}