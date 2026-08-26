package by.dytni.gateway.service.impl;


import static by.dytni.gateway.GatewayConstants.HttpHeader.BLACKLIST_PREFIX;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import by.dytni.gateway.service.ReactiveUserStatusService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveUserStatusServiceImpl implements ReactiveUserStatusService {


    private final ReactiveRedisTemplate<String, Object> redisTemplate;


    public Mono<Boolean> isActive(Long userId) {


        return redisTemplate.hasKey(BLACKLIST_PREFIX + userId)
                .map(isBlacklisted -> !isBlacklisted)
                .onErrorReturn(false);
    }
}
