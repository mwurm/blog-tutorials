package de.rieckpil.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

// JUnit 5 example with Spring Boot >= 2.2.6
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CreatePersonIT extends AbstractContainerBaseTest {

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  public TestRestTemplate testRestTemplate;

  @Test
  @Sql(value = "/testdata/FILL_FOUR_PERSONS_CLEANUP.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testRestEndpointForAllPersons() {

    Person requestBody = new Person();
    requestBody.setName("rieckpil");

    assertEquals(0, personRepository.findAll().size());

    ResponseEntity<Person> result = testRestTemplate.postForEntity("/api/persons", requestBody, Person.class);

    assertNotNull(result);
    assertNotNull(result.getBody().getId());
    assertEquals("rieckpil", result.getBody().getName());
    assertEquals(1, personRepository.findAll().size());
    assertEquals("rieckpil", personRepository.findAll().get(0).getName());
  }

}
