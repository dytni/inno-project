package by.dytni.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import by.dytni.commonevents.config.KafkaConsumerConfig;
import by.dytni.commonhibernate.repository.config.JpaConfig;

@Import({
        JpaConfig.class,
        KafkaConsumerConfig.class,
})
@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
