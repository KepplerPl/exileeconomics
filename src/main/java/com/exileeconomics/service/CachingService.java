package com.exileeconomics.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CachingService {
    private final Gson gson = new Gson();

    private final RedisTemplate<String, String> redisTemplate;

    public CachingService(@Autowired RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(final String key, final Object object, final int ttl, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, gson.toJson(object));
        redisTemplate.expire(key, ttl, timeUnit);
    }

    public String get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean delete(final String key) {
        return redisTemplate.delete(key);
    }
}
