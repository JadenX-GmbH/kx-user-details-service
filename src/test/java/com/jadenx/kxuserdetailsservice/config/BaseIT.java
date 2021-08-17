package com.jadenx.kxuserdetailsservice.config;

import com.jadenx.kxuserdetailsservice.KxUserDetailsServiceApplication;
import com.jadenx.kxuserdetailsservice.repos.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.MariaDBContainer;

import java.nio.charset.StandardCharsets;


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context with a
 * Datasource connected to the Testcontainers Docker instance. The instance is reused for all tests,
 * with all data wiped out before each test.
 */
// CHECKSTYLE IGNORE check FOR NEXT 4 LINES
@SpringBootTest(
    classes = {KxUserDetailsServiceApplication.class, TestSecurityConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("it")
@Sql("/data/clearAll.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIT {

    // MySQL container supporting Apple M1 is not available. Mariadb is a MySQL fork
    @SuppressWarnings("rawtypes")
    private static final MariaDBContainer mariaDbContainer;

    static {
        //noinspection rawtypes
        mariaDbContainer = (MariaDBContainer) (new MariaDBContainer("mariadb")
            .withUsername("testcontainers")
            .withPassword("Testcontain3rs!")
            .withReuse(true));
        mariaDbContainer.start();
    }

    @Autowired
    public TestRestTemplate restTemplate;

    @Autowired
    public SkillsetRepository skillsetRepository;

    @Autowired
    public SkillRepository skillRepository;

    @Autowired
    public CategoryRepository categoryRepository;

    @Autowired
    public AddressRepository addressRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public GigRepository gigRepository;

    @Autowired
    public DetailsRepository detailsRepository;

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDbContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mariaDbContainer::getPassword);
        registry.add("spring.datasource.username", mariaDbContainer::getUsername);
    }

    @SneakyThrows
    public String readResource(final String resourceName) {
        return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), StandardCharsets.UTF_8);
    }

    public HttpHeaders headers() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
