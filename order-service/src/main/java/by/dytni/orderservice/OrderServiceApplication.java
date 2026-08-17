package by.dytni.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "by.dytni.orderservice",
                "by.dytni.commonhibernate",
                "by.dytni.commonsecurity",
                "by.dytni.commonredis"
        }
)
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
