package by.dytni.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import by.dytni.paymentservice.client.fallback.RandomNumberClientFallbackFactory;
import by.dytni.paymentservice.dto.RandomNumber;

@FeignClient(
        name = "randomNumber",
        url = "${app.external-api.random-number.base-url}",
        fallbackFactory = RandomNumberClientFallbackFactory.class
)
public interface RandomNumberClient {

    @GetMapping()
    RandomNumber generateRandomNumber();

}
