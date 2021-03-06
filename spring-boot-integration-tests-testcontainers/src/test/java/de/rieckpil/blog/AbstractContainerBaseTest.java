package de.rieckpil.blog;

import org.junit.Before;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

abstract class AbstractContainerBaseTest {

    static final PostgreSQLContainer POSTGRESQL_CONTAINER;

    static {
      POSTGRESQL_CONTAINER = new PostgreSQLContainer()
        .withPassword("inmemory")
        .withUsername("inmemory");
      POSTGRESQL_CONTAINER.start();
    }

  @DynamicPropertySource
  static void postgresqlProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
  }

}
