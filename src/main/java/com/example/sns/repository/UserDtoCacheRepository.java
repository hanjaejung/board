package com.example.sns.repository;

import com.example.sns.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDtoCacheRepository {

    private final RedisTemplate<String, UserDto> userRedisTemplate;

    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);


    public void setUser(UserDto user) {
        String key = getKey(user.getUsername());
        log.info("Set User to Redis {}({})", key, user);
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<UserDto> getUser(String userName) {
        UserDto data = userRedisTemplate.opsForValue().get(getKey(userName));
        log.info("Get User from Redis {}", data);
        return Optional.ofNullable(data);
    }


    private String getKey(String userName) {
        return "UID:" + userName;
    }
}
