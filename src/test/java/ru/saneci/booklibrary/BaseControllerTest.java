package ru.saneci.booklibrary;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.File;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@DirtiesContext
@Testcontainers
@SpringBootTest
public abstract class BaseControllerTest {

    protected MockMvc mockMvc;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0")
                .withReuse(true)
                // TODO remove withCopyFileToContainer() after create automatic database migrations
                .withCopyFileToContainer(
                        getDatabaseMigrationFile("r-2023-1/deploy-23-1.sql"),
                        getContainerInitDatabasePath("deploy-23-1.sql"))
                .withCopyFileToContainer(
                        getDatabaseMigrationFile("r-2023-2/deploy-23-2.sql"),
                        getContainerInitDatabasePath("deploy-23-2.sql"))
                .withCopyFileToContainer(
                        getDatabaseMigrationFile("r-2024-1/deploy-24-1.sql"),
                        getContainerInitDatabasePath("deploy-24-1.sql"))
                .withCopyFileToContainer(
                        getDatabaseMigrationFile("r-2024-2/deploy-24-2.sql"),
                        getContainerInitDatabasePath("deploy-24-2.sql"));
    }

    private static MountableFile getDatabaseMigrationFile(String fileName) {
        return MountableFile.forHostPath(String.format("src/main/resources/db/migration/%s", fileName)
                .replace("/", File.separator));
    }

    private static String getContainerInitDatabasePath(String suffix) {
        return String.format("/docker-entrypoint-initdb.d/%s", suffix).replace("/", File.separator);
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }
}
