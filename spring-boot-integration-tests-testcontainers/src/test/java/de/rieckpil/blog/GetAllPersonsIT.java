package de.rieckpil.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

// JUnit 5 example with Spring Boot >= 2.2.6
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GetAllPersonsIT extends AbstractContainerBaseTest {
  @Autowired
  public TestRestTemplate testRestTemplate;

  @Test
  @Sql("/testdata/FILL_FOUR_PERSONS.sql")
  @Sql(value = "/testdata/FILL_FOUR_PERSONS_CLEANUP.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testGetAllPersons() {

    ResponseEntity<Person[]> result = testRestTemplate.getForEntity("/api/persons", Person[].class);

    List<Person> resultList = Arrays.asList(result.getBody());

    assertEquals(4, resultList.size());
    assertTrue(resultList.stream().map(p -> p.getName()).collect(Collectors.toList()).containsAll(Arrays.asList
      ("Mike", "Phil", "Duke", "Tom")));

  }

}
