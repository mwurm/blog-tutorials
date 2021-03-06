package de.rieckpil.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

// JUnit 5 example with Spring Boot >= 2.2.6
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GetPersonByIdIT extends AbstractContainerBaseTest {

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
  @Sql(value = "/testdata/FILL_FOUR_PERSONS_CLEANUP.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testExistingPersonById() {
    System.out.println(personRepository.findAll().size());

    ResponseEntity<Person> result = testRestTemplate.getForEntity("/api/persons/2", Person.class);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("Mike", result.getBody().getName());
    assertEquals(2l, result.getBody().getId().longValue());
  }

}
