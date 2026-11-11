package com.blog.config;

import com.blog.common.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 限流 AOP 切面
 *
 * 基于 Redis 滑动窗口 (Lua 脚本保证原子性)
 *
 * 面试可讲:
 *  - 为什么用 Lua?  保证判断+计数在 Redis 里原子执行, 避免并发击穿
 *  - Redis 集群下怎么办?  Lua 脚本要保证所有 key 落在同一 slot (可用 hash tag)
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final StringRedisTemplate redisTemplate;

    /**
     * Lua 脚本: 滑动窗口限流
     * KEY[1] = 限流 key
     * ARGV[1] = 当前时间戳 (ms)
     * ARGV[2] = 窗口起始时间 (ms)
     * ARGV[3] = 最大允许请求数
     * ARGV[4] = 窗口过期时间 (s)
     */
    private static final String LUA_SCRIPT = """
            local key   = KEYS[1]
            local now   = tonumber(ARGV[1])
            local start = tonumber(ARGV[2])
            local max   = tonumber(ARGV[3])
            local ttl   = tonumber(ARGV[4])
            
            -- 1. 移除窗口外的旧记录
            redis.call('ZREMRANGEBYSCORE', key, 0, start)
            
            -- 2. 统计窗口内请求数
            local count = redis.call('ZCARD', key)
            
            -- 3. 超过阈值则拒绝
            if count >= max then
                return 0
            end
            
            -- 4. 写入当前请求记录 (用 member + score 防止同一毫秒覆盖)
            redis.call('ZADD', key, now, now .. '-' .. math.random())
            redis.call('EXPIRE', key, ttl)
            return 1
            """;

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String methodKey = rateLimit.key().isBlank()
                ? pjp.getTarget().getClass().getSimpleName() + ":" + sig.getName()
                : rateLimit.key();

        String redisKey = "ratelimit:" + methodKey;

        long windowMs = rateLimit.unit().toMillis(rateLimit.seconds());
        long now = System.currentTimeMillis();
        long start = now - windowMs;
        long ttlSec = rateLimit.unit().toSeconds(rateLimit.seconds()) + 1;

        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(LUA_SCRIPT, Long.class),
                Collections.singletonList(redisKey),
                String.valueOf(now),
                String.valueOf(start),
                String.valueOf(rateLimit.permits()),
                String.valueOf(ttlSec)
        );

        if (result == null || result == 0L) {
            log.warn("触发限流: key={}, permits={}, window={}ms", redisKey, rateLimit.permits(), windowMs);
            throw new BusinessException("请求过于频繁, 请稍后再试 (每" + rateLimit.seconds() + "秒最多" + rateLimit.permits() + "次)");
        }

        return pjp.proceed();
    }
}
