package com.wangaixi.redisdemo.service;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // ========== 字符串操作 ==========
    public void setString(String key, Object value, long expire) {
        if (expire > 0) {
            redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    public Object getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean deleteString(String key) {
        return redisTemplate.delete(key);
    }

    // ========== 哈希操作 ==========
    public void hashPut(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public Object hashGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public Map<Object, Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void hashDelete(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    // ========== 列表操作 ==========
    public Long listLeftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public Long listRightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public Object listLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public Object listRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public List<Object> listRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }
}