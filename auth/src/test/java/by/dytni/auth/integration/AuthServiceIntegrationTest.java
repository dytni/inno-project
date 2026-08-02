package by.dytni.auth.integration;


import static by.dytni.auth.AuthTestConstants.TEST_ADMIN_LOGIN;
import static by.dytni.auth.AuthTestConstants.TEST_ADMIN_PASSWORD;
import static by.dytni.auth.AuthTestConstants.TEST_ADMIN_WRONG_PASSWORD;
import static by.dytni.auth.AuthTestConstants.TEST_USER_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import by.dytni.auth.config.TestSecurityConfig;
import by.dytni.auth.dto.auth.AuthRequest;
import by.dytni.auth.dto.JwtResponse;
import by.dytni.auth.dto.register.RegisterRequest;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class AuthServiceIntegrationTest {

    private static final String POSTGRES_PASSWORD = UUID.randomUUID().toString();
    private static final String BASE_URL = "/api/auth";

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
    void register_user() {
        RegisterRequest request = createRegisterRequest();

        ResponseEntity<JwtResponse> response =
                restTemplate.postForEntity(
                        BASE_URL + "/register",
                        request,
                        JwtResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        JwtResponse body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getAccessToken()).isNotBlank();
        assertThat(body.getRefreshToken()).isNotBlank();
    }

    @Test
    void login_with_wrong_password(){

        AuthRequest request = AuthRequest.builder()
                .login(TEST_ADMIN_LOGIN)
                .password(TEST_ADMIN_WRONG_PASSWORD)
                .build();


        ResponseEntity<JwtResponse> response =
                restTemplate.postForEntity(
                        BASE_URL + "/login",
                        request,
                        JwtResponse.class
                );


        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void make_admin(){

        RegisterRequest registerRequest = createRegisterRequest();
        restTemplate.postForEntity(
                BASE_URL + "/register",
                registerRequest,
                JwtResponse.class);

        String token = getAdminToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<RegisterRequest> request =
                new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                BASE_URL + "/admin?login=" + registerRequest.getLogin(),
                HttpMethod.PUT,
                request,
                Void.class
        );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void validate_token(){

        String token = getAdminToken();

        ResponseEntity<Void> response =
                restTemplate.getForEntity(
                        BASE_URL + "/validate?token=" + token,
                        Void.class
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }



    private String getAdminToken() {

        AuthRequest request = AuthRequest.builder()
                .login(TEST_ADMIN_LOGIN)
                .password(TEST_ADMIN_PASSWORD)
                .build();

        ResponseEntity<JwtResponse> response =
                restTemplate.postForEntity(
                        BASE_URL + "/login",
                        request,
                        JwtResponse.class
                );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response.getBody().getAccessToken();
    }


    private RegisterRequest createRegisterRequest() {
        return RegisterRequest.builder()
                .login(UUID.randomUUID() + "@gmail.com")
                .password(TEST_USER_PASSWORD)
                .build();
    }

}
