package com.wangaixi.redisdemo.test;

import com.wangaixi.redisdemo.config.RedisCommonOperations;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class RedisDemoService {

    @Resource
    private RedisCommonOperations redisOps;

    public void demo() {
        // 1. 字符串缓存（5分钟过期）
        redisOps.setWithExpire("user:name:1", "张三", 5, TimeUnit.MINUTES);
        System.out.println(redisOps.get("user:name:1")); // 输出：张三

        // 2. 计数器自增
        redisOps.increment("article:like:100", 1); // 文章100的点赞数+1

        // 3. Hash存储用户信息
        redisOps.hashPut("user:info:1", "age", 25);
        redisOps.hashPut("user:info:1", "gender", "男");
        System.out.println(redisOps.hashGet("user:info:1", "age")); // 输出：25

        // 4. ZSet排行榜（添加两个用户的分数）
        redisOps.zSetAdd("rank:score", "用户A", 95.5);
        redisOps.zSetAdd("rank:score", "用户B", 88.0);
        System.out.println(redisOps.zSetReverseRange("rank:score", 0, 1)); // 降序输出：[用户A, 用户B]

        // 5. 分布式锁
        String lockKey = "lock:order:100";
        String lockValue = java.util.UUID.randomUUID().toString();
        if (redisOps.getDistributedLock(lockKey, lockValue, 30)) {
            try {
                // 执行业务逻辑（如创建订单）
                System.out.println("获取锁成功，执行业务");
            } finally {
                // 释放锁
                redisOps.releaseDistributedLock(lockKey, lockValue);
            }
        } else {
            System.out.println("获取锁失败，请勿重复操作");
        }
    }

}