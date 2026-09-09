package by.dytni.paymentservice.client.fallback;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import by.dytni.paymentservice.client.RandomNumberClient;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RandomNumberClientFallbackFactory implements FallbackFactory<RandomNumberClient> {

    @Override
    public RandomNumberClient create(Throwable cause) {
        log.error("Random number service failed", cause);
        return () -> {
            throw new RuntimeException(cause);
        };
    }

}
