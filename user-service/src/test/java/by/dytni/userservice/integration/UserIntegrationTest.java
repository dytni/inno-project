package by.dytni.userservice.integration;

import static by.dytni.userservice.UserServiceTestConstants.USER_ANOTHER_FIRST_NAME;
import static by.dytni.userservice.UserServiceTestConstants.USER_BIRTH_DATE;
import static by.dytni.userservice.UserServiceTestConstants.USER_EMAIL;
import static by.dytni.userservice.UserServiceTestConstants.USER_FIRST_NAME;
import static by.dytni.userservice.UserServiceTestConstants.USER_LAST_NAME;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import by.dytni.userservice.config.AuditTestConfig;
import by.dytni.userservice.config.SecurityTestConfig;
import by.dytni.userservice.dto.user.User;
import by.dytni.userservice.dto.user.UserMaker;
import by.dytni.userservice.dto.user.UserUpdater;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Import({
        SecurityTestConfig.class,
        AuditTestConfig.class
})
@Transactional
public class UserIntegrationTest{

    private static final String BASE_URL = "/api/user";

    private static final String POSTGRES_PASSWORD = UUID.randomUUID().toString();

    @Container
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword(POSTGRES_PASSWORD);

    @Container
    static GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @Container
    static KafkaContainer kafka =
            new KafkaContainer(
                    DockerImageName.parse("apache/kafka:3.8.0"));


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }


    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void create_user() {
        UserMaker request = createUserRequest();

        ResponseEntity<User> response =
                restTemplate.postForEntity(BASE_URL, request, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        User body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getFirstName()).isEqualTo(USER_FIRST_NAME);
        assertThat(body.getLastName()).isEqualTo(USER_LAST_NAME);
    }

    @Test
    void get_user_by_id() {
        User created = createUser();

        assertThat(created).isNotNull();

        ResponseEntity<User> response =
                restTemplate.getForEntity(BASE_URL + "/" + created.getId(), User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        User body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(created.getId());
        assertThat(body.getFirstName()).isEqualTo(USER_FIRST_NAME);
        assertThat(body.getLastName()).isEqualTo(USER_LAST_NAME);
    }

    @Test
    void update_user() {
        User created = createUser();
        assertThat(created).isNotNull();
        UserUpdater updater = UserUpdater.builder()
                .firstName(USER_ANOTHER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .build();

        HttpEntity<UserUpdater> entity = new HttpEntity<>(updater);


        ResponseEntity<User> response = restTemplate.exchange(
                BASE_URL + "/" + created.getId(),
                HttpMethod.PUT,
                entity,
                User.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        User body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(created.getId());
        assertThat(body.getFirstName()).isEqualTo(USER_ANOTHER_FIRST_NAME);
        assertThat(body.getLastName()).isEqualTo(USER_LAST_NAME);
    }

    @Test
    void get_all_users() {
        createUser();
        ResponseEntity<String> response =
                restTemplate.getForEntity(BASE_URL, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(USER_FIRST_NAME);
        assertThat(response.getBody()).contains(USER_LAST_NAME);
    }

    @Test
    void change_user_status() {
        User created = createUser();
        assertThat(created).isNotNull();

        ResponseEntity<User> response = restTemplate.exchange(
                BASE_URL + "/active/" + created.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                User.class
        );


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        User body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(created.getId());
    }

    private UserMaker createUserRequest() {
        return UserMaker.builder()
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(UUID.randomUUID() + "@gmail.com")
                .birthDate(USER_BIRTH_DATE)
                .build();
    }

    private User createUser() {
        return restTemplate
                .postForEntity(BASE_URL, createUserRequest(), User.class)
                .getBody();
    }

}
