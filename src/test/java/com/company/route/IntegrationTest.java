package com.company.route;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Spring Boot Integration tests base class. It launches Spring boot test server on random port. It activates test
 * profile. It applies @Transactional Spring test behavior - all tests are wrapped in transaction and rolled back
 * automatically This ensures clean state in database.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {RouteServiceApplication.class}, webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public abstract class IntegrationTest {

  @Autowired
  protected WebApplicationContext context;

  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  /**
   * Prepares reusable mockMvc by assigning Spring Web App Context.
   */
  @BeforeEach
  public void prepareMockMvc() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }
}
