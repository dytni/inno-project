package by.dytni.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import by.dytni.commonevents.config.KafkaConsumerConfig;

@Import(KafkaConsumerConfig.class)
@SpringBootApplication(
        scanBasePackages = {
                "by.dytni.commonhibernate",
                "by.dytni.commonsecurity",
                "by.dytni.commonredis",
                "by.dytni.auth"
        }
)
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
