package com.wangaixi.redisdemo.config;

import jakarta.annotation.Resource;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCommonOperations {

    // 注入配置好的RedisTemplate
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // ====================== 1. 字符串（String）- 最常用 ======================
    /**
     * 设置字符串值（无过期时间）
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置字符串值（带过期时间）
     * @param timeout 过期时间
     * @param unit 时间单位（TimeUnit.SECONDS/Minutes/HOURS/DAYS）
     */
    public void setWithExpire(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取字符串值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除指定key
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 自增（整数，如计数器、点赞数）
     * @param delta 自增步长（默认1，负数则自减）
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 自增（浮点数）
     */
    public Double incrementFloat(String key, double delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    // ====================== 2. 哈希（Hash）- 存储对象/结构化数据 ======================
    /**
     * 向Hash中存入单个字段
     */
    public void hashPut(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 向Hash中存入多个字段
     */
    public void hashPutAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取Hash中单个字段值
     */
    public Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取Hash中所有字段和值
     */
    public Map<Object, Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取Hash中所有字段名
     */
    public Set<Object> hashGetKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * Hash字段自增
     */
    public Long hashIncrement(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 删除Hash中指定字段
     */
    public Long hashDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    // ====================== 3. 列表（List）- 队列/栈/消息流 ======================
    /**
     * 左推（从列表头部添加元素，栈/队列入队）
     */
    public Long listLeftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 右推（从列表尾部添加元素）
     */
    public Long listRightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 左弹（从列表头部取出元素，栈出栈/队列出队）
     */
    public Object listLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 右弹（从列表尾部取出元素）
     */
    public Object listRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取列表指定范围的元素（0=-1 表示所有）
     */
    public List<Object> listRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取列表长度
     */
    public Long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    // ====================== 4. 集合（Set）- 去重/交集/并集 ======================
    /**
     * 向Set中添加元素
     */
    public Long setAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取Set中所有元素
     */
    public Set<Object> setMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断元素是否在Set中
     */
    public Boolean setIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 移除Set中指定元素
     */
    public Long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 求两个Set的交集
     */
    public Set<Object> setIntersect(String key1, String key2) {
        return redisTemplate.opsForSet().intersect(key1, key2);
    }

    /**
     * 求两个Set的并集
     */
    public Set<Object> setUnion(String key1, String key2) {
        return redisTemplate.opsForSet().union(key1, key2);
    }

    // ====================== 5. 有序集合（ZSet）- 排行榜/权重排序 ======================
    /**
     * 向ZSet中添加元素（带分数，分数用于排序）
     */
    public Boolean zSetAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 按分数升序获取指定范围元素（0=-1 表示所有）
     */
    public Set<Object> zSetRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 按分数降序获取指定范围元素（排行榜常用）
     */
    public Set<Object> zSetReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取元素的分数
     */
    public Double zSetScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 元素分数自增（点赞排行榜加分）
     */
    public Double zSetIncrementScore(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * 获取元素的排名（升序，从0开始）
     */
    public Long zSetRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    // ====================== 6. 通用操作 ======================
    /**
     * 判断key是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置key的过期时间
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取key的过期时间
     * @return 剩余秒数（-1：永久有效，-2：key不存在）
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 移除key的过期时间（设为永久）
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    // ====================== 7. 分布式锁（常用实战场景） ======================
    /**
     * 获取分布式锁（SET NX EX 实现，原子操作）
     * @param lockKey 锁key
     * @param value 唯一值（如UUID，用于释放锁时校验）
     * @param expireTime 锁过期时间（避免死锁）
     * @return 是否获取成功
     */
    public Boolean getDistributedLock(String lockKey, String value, long expireTime) {
        return redisTemplate.opsForValue().setIfAbsent(lockKey, value, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 释放分布式锁（需保证原子性，推荐用Lua脚本，避免误删）
     * @param lockKey 锁key
     * @param value 唯一值（与获取锁时一致）
     * @return 是否释放成功
     */
    public Boolean releaseDistributedLock(String lockKey, String value) {
        // Lua脚本：判断value一致才删除，保证原子性
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        return redisTemplate.execute(
                (RedisCallback<Boolean>) connection -> {
                    Object result = connection.eval(
                            luaScript.getBytes(),
                            ReturnType.INTEGER,
                            1,
                            lockKey.getBytes(),
                            value.getBytes()
                    );
                    return "1".equals(result.toString());
                }
        );
    }
}