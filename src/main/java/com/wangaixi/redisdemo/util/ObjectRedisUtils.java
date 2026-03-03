package com.wangaixi.redisdemo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class ObjectRedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    // 注入我们之前配置的自定义ObjectMapper
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 存入自定义对象（无过期时间）
     */
    public <T> void setObject(String key, T obj) {
        redisTemplate.opsForValue().set(key, obj);
    }

    /**
     * 存入自定义对象（带过期时间）
     */
    public <T> void setObjectWithExpire(String key, T obj, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, obj, timeout, unit);
    }

    /**
     * 读取自定义对象（核心优化：手动反序列化，指定目标类型）
     */
    public <T> T getObject(String key, Class<T> clazz) {
        // 1. 先读取Redis中的原始值
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }

        // 2. 如果已经是目标类型，直接返回（避免重复序列化）
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }

        // 3. 手动反序列化：将LinkedHashMap转为目标对象
        try {
            // 先将value转为JSON字符串，再反序列化为目标类型
            String jsonStr = objectMapper.writeValueAsString(value);
            return objectMapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Redis反序列化对象失败", e);
        }
    }

    /**
     * 读取集合类型（如List<User>，扩展方法）
     */
    public <T> T getObject(String key, TypeReference<T> typeReference) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        try {
            String jsonStr = objectMapper.writeValueAsString(value);
            return objectMapper.readValue(jsonStr, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Redis反序列化集合对象失败", e);
        }
    }

    /**
     * 删除对象
     */
    public Boolean deleteObject(String key) {
        return redisTemplate.delete(key);
    }
}