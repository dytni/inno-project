package by.dytni.commonsecurity.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import by.dytni.commonsecurity.service.UserStatusService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisUserStatusService implements UserStatusService {

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    private static final String PREFIX = "inactive_user:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isActive(Long userId) {

        return !Boolean.TRUE.equals(
                redisTemplate.hasKey(PREFIX + userId)
        );
    }

    @Override
    public void deactivate(Long userId) {

        redisTemplate.opsForValue().set(
                PREFIX + userId,
                true,
                accessExpiration
        );
    }

    @Override
    public void activate(Long userId) {

        redisTemplate.delete(PREFIX + userId);
    }
}
