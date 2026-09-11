package by.dytni.commonsecurity.service.impl;

import static by.dytni.commonsecurity.CommonSecurityConstant.BLACKLIST_PREFIX;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import by.dytni.commonsecurity.service.UserStatusService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "redis.enabled",
        havingValue = "true"
)
public class RedisUserStatusService implements UserStatusService {

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;


    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isActive(Long userId) {

        return !Boolean.TRUE.equals(
                redisTemplate.hasKey(BLACKLIST_PREFIX + userId)
        );
    }

    @Override
    public void deactivate(Long userId) {

        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + userId,
                true,
                accessExpiration
        );
    }

    @Override
    public void activate(Long userId) {
        redisTemplate.delete(BLACKLIST_PREFIX + userId);
    }
}
