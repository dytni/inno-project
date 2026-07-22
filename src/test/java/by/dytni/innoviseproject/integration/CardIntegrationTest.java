package by.dytni.innoviseproject.integration;

import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.CARD_ACTIVE;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.CARD_ANOTHER_NUMBER;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.CARD_NUMBER;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_BIRTH_DATE;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_EMAIL;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_FIRST_NAME;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_LAST_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
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

import by.dytni.innoviseproject.dto.card.Card;
import by.dytni.innoviseproject.dto.card.CardMaker;
import by.dytni.innoviseproject.dto.card.CardUpdater;
import by.dytni.innoviseproject.dto.user.User;
import by.dytni.innoviseproject.dto.user.UserMaker;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CardIntegrationTest{
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


    private static final String BASE_URL = "/api/card";

    private User user;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        UserMaker request = UserMaker.builder()
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .build();

        ResponseEntity<User> response =
                restTemplate.postForEntity("/api/user", request, User.class);

        user = response.getBody();
    }

    @Test
    void create_card() {
        CardMaker request = CardMaker.builder()
                .userId(user.getId())
                .cardNumber(CARD_NUMBER)
                .build();

        ResponseEntity<Card> response =
                restTemplate.postForEntity(BASE_URL, request, Card.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Card body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getActiveStatus()).isEqualTo(CARD_ACTIVE);
        assertThat(body.getExpiryDate().isEqual(LocalDate.now().plusYears(5)));
    }

    @Test
    void get_card_by_id() {
        CardMaker request = CardMaker.builder()
                .userId(user.getId())
                .cardNumber(CARD_NUMBER)
                .build();

        Card created = restTemplate
                .postForEntity(BASE_URL, request, Card.class)
                .getBody();
        assertThat(created).isNotNull();

        ResponseEntity<Card> response =
                restTemplate.getForEntity(BASE_URL + "/" + created.getId(), Card.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Card body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(created.getId());
    }

    @Test
    void update_card() {
        CardMaker request = CardMaker.builder()
                .userId(user.getId())
                .cardNumber(CARD_NUMBER)
                .build();

        Card created = restTemplate
                .postForEntity(BASE_URL, request, Card.class)
                .getBody();
        assertThat(created).isNotNull();

        CardUpdater updater =  CardUpdater.builder()
                .cardNumber(CARD_ANOTHER_NUMBER)
                .build();

        HttpEntity<CardUpdater> entity = new HttpEntity<>(updater);

        ResponseEntity<Card> response = restTemplate.exchange(
                BASE_URL + "/" + created.getId(),
                HttpMethod.PUT,
                entity,
                Card.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Card body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(created.getId());
        assertThat(body.getCardNumber()).isEqualTo(CARD_ANOTHER_NUMBER);
    }
    @Test
    void get_all_cards() {
        CardMaker request = CardMaker.builder()
                .userId(user.getId())
                .cardNumber(CARD_NUMBER)
                .build();

        restTemplate.postForEntity(BASE_URL, request, Card.class);

        ResponseEntity<String> response =
                restTemplate.getForEntity(BASE_URL, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(CARD_NUMBER);
    }

    @Test
    void change_card_status() {
        CardMaker request = CardMaker.builder()
                .userId(user.getId())
                .cardNumber(CARD_NUMBER)
                .build();

        Card created = restTemplate
                .postForEntity(BASE_URL, request, Card.class)
                .getBody();

        assertThat(created).isNotNull();

        ResponseEntity<Card> response = restTemplate.exchange(
                BASE_URL + "/active/" + created.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Card.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Card body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(created.getId());
    }



}
