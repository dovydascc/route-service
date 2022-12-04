package com.company.route.features.countries.entities;

import java.util.List;

/**
 * Route Entity is a route between countries (For example, a route from France to Poland)
 *
 * @param countries A list of countries
 */
public record Route(List<String> countries) {

}
