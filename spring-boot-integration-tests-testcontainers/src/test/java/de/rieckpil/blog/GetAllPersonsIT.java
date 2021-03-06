package de.rieckpil.blog;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

// JUnit 4.12 example
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GetAllPersonsIT {
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
  public TestRestTemplate testRestTemplate;

  @Test
  @Sql("/testdata/FILL_FOUR_PERSONS.sql")
  public void testGetAllPersons() {

    ResponseEntity<Person[]> result = testRestTemplate.getForEntity("/api/persons", Person[].class);

    List<Person> resultList = Arrays.asList(result.getBody());

    assertEquals(4, resultList.size());
    assertTrue(resultList.stream().map(p -> p.getName()).collect(Collectors.toList()).containsAll(Arrays.asList
      ("Mike", "Phil", "Duke", "Tom")));

  }

}
