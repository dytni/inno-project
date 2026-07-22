package by.dytni.innoviseproject.integration;

import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_ANOTHER_FIRST_NAME;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_BIRTH_DATE;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_EMAIL;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_FIRST_NAME;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_LAST_NAME;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import by.dytni.innoviseproject.dto.user.User;
import by.dytni.innoviseproject.dto.user.UserMaker;
import by.dytni.innoviseproject.dto.user.UserUpdater;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserIntegrationTest{

    private static final String BASE_URL = "/api/user";

    private static final String POSTGRES_PASSWORD = UUID.randomUUID().toString();

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword(POSTGRES_PASSWORD);


    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7")
            .withExposedPorts(6379);


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }



    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void create_user() {
        UserMaker request = UserMaker.builder()
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .build();

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
        UserMaker request = UserMaker.builder()
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .build();

        User created = restTemplate
                .postForEntity(BASE_URL, request, User.class)
                .getBody();

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
        UserMaker request = UserMaker.builder()
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .build();

        User created = restTemplate
                .postForEntity(BASE_URL, request, User.class)
                .getBody();

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
        UserMaker request = UserMaker.builder()
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .build();

        restTemplate.postForEntity(BASE_URL, request, User.class);

        ResponseEntity<String> response =
                restTemplate.getForEntity(BASE_URL, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(USER_FIRST_NAME);
        assertThat(response.getBody()).contains(USER_LAST_NAME);
    }

    @Test
    void change_user_status() {
        UserMaker request = UserMaker.builder()
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .build();

        User created = restTemplate
                .postForEntity(BASE_URL, request, User.class)
                .getBody();


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



}
