package de.rieckpil.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

// JUnit 4.12 example
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GetPersonByIdIT {

  @Container
  public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
    .withPassword("inmemory")
    .withUsername("inmemory");

  @DynamicPropertySource
  static void postgresqlProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
  }


  @Autowired
  private PersonRepository personRepository;

  @Autowired
  public TestRestTemplate testRestTemplate;

  @Test
  public void testNotExistingPersonByIdShouldReturn404() {

    ResponseEntity<Person> result = testRestTemplate.getForEntity("/api/persons/42", Person.class);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    assertNull(result.getBody().getName());
    assertNull(result.getBody().getId());

  }

  @Test
  @Sql("/testdata/FILL_FOUR_PERSONS.sql")
  public void testExistingPersonById() {
    System.out.println(personRepository.findAll().size());

    ResponseEntity<Person> result = testRestTemplate.getForEntity("/api/persons/1", Person.class);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("Phil", result.getBody().getName());
    assertEquals(1l, result.getBody().getId().longValue());
  }

}
