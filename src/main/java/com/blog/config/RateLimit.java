package com.blog.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 接口限流注解
 *
 * 基于 Redis + 滑动窗口实现
 * 用法: @RateLimit(permits = 10, seconds = 60)  => 60秒内最多10次
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /** 时间窗口内允许的最大请求数 */
    long permits() default 10;

    /** 时间窗口长度 */
    long seconds() default 60;

    /** 时间窗口单位 */
    TimeUnit unit() default TimeUnit.SECONDS;

    /** 限流 Key (不填则用 类名+方法名) */
    String key() default "";
}
