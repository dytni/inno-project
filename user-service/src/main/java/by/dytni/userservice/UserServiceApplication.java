package by.dytni.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(
        scanBasePackages = {
                "by.dytni.userservice",
                "by.dytni.commonevents",
                "by.dytni.commonhibernate",
                "by.dytni.commonsecurity",
                "by.dytni.commonredis"
        }
)
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
