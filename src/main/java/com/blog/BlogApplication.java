package com.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 博客管理系统 - 启动类
 *
 * 技术栈: Spring Boot 3.2 + MyBatis-Plus + Redis + JWT + Knife4j
 *
 * @author blog-system
 */
@SpringBootApplication
@MapperScan("com.blog.mapper")          // MyBatis-Plus Mapper 扫描
@EnableCaching                           // 启用 Redis 缓存
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
        System.out.println("""
                ========================================
                  博客系统启动成功!
                  Swagger UI: http://localhost:8080/doc.html
                  API Docs:   http://localhost:8080/v3/api-docs
                ========================================
                """);
    }
}
