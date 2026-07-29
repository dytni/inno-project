package by.dytni.auth.integration;

import static by.dytni.auth.AuthTestConstants.TEST_ADMIN_ROLE;
import static by.dytni.auth.AuthTestConstants.TEST_USER_PASSWORD;
import static by.dytni.auth.AuthTestConstants.TEST_USER_ROLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import by.dytni.auth.dto.AuthRequest;
import by.dytni.auth.dto.JwtResponse;
import by.dytni.auth.dto.RegisterRequest;
import by.dytni.auth.repository.UserRepository;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public class AuthServiceIntegrationTest {

    private static final String POSTGRES_PASSWORD = UUID.randomUUID().toString();
    private static final String BASE_URL = "/api/auth";

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword(POSTGRES_PASSWORD);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void register_user() {
        String token = getAdminToken();

        RegisterRequest request = createRegisterRequest();


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<RegisterRequest> entity =
                new HttpEntity<>(request, headers);


        ResponseEntity<JwtResponse> response =
                restTemplate.postForEntity(
                        BASE_URL + "/register",
                        entity,
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
                .login("admin")
                .password("1234")
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
                .login("admin")
                .password("1111")
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
                .role(TEST_USER_ROLE)
                .build();
    }

}
