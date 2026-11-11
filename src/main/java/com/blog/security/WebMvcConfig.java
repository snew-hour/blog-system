package com.blog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 *
 * 注册 JwtInterceptor, 并配置拦截路径 / 白名单
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")                     // 拦截所有路径
                .excludePathPatterns(                       // 放行白名单
                        // ===== 认证接口 =====
                        "/auth/login",
                        "/auth/register",

                        // ===== 健康检查 =====
                        "/health",

                        // ===== 公开查询接口 =====
                        "/blogs",            // 文章列表 (无需登录)
                        "/blogs/**/comments", // 评论列表
                        "/categories",       // 分类列表

                        // ===== API 文档 =====
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/favicon.ico",

                        // ===== H2 控制台（开发环境） =====
                        "/h2-console/**",

                        // ===== 静态资源 =====
                        "/error"
                );
    }
}
