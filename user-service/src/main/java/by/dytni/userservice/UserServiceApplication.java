package by.dytni.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import by.dytni.commonevents.config.KafkaProducerConfig;
import by.dytni.commonhibernate.repository.config.JpaConfig;

@SpringBootApplication(
        scanBasePackages = {
                "by.dytni.userservice",
                "by.dytni.commonevents",
                "by.dytni.commonhibernate"
        }
)
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
