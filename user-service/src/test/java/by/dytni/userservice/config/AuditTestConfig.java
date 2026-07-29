package by.dytni.userservice.config;

import static by.dytni.userservice.UserServiceConstantsTest.CREATED_BY;

import java.util.Optional;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;

@TestConfiguration
public class AuditTestConfig {

    @Bean
    @Primary
    AuditorAware<String> auditorAware() {
        return () -> Optional.of(CREATED_BY);
    }
}

