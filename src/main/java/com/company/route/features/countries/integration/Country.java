package com.company.route.features.countries.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Country is an Inbound DTO for communication with our GitHub Countries.json data source.
 *
 * @param abbreviation Country abbreviation 3 letters, e.g. USA
 * @param borders      Neighbor countries abbreviations
 */
public record Country(@JsonProperty("cca3") String abbreviation, List<String> borders) {

}
