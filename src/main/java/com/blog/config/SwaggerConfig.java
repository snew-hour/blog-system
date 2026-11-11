package com.blog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j (OpenAPI 3.0) 配置
 *
 * 访问地址: http://localhost:8080/api/doc.html
 */
@Configuration
public class SwaggerConfig {

    private static final String AUTH_HEADER = "Authorization";

    @Bean
    public OpenAPI blogOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("博客管理系统 API")
                        .description("基于 Spring Boot 3 + MyBatis-Plus + Redis + JWT 的博客系统接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Blog System")
                                .email("dev@blog.com")))
                // 全局安全声明: 所有接口都可以带 Authorization Header
                .addSecurityItem(new SecurityRequirement().addList(AUTH_HEADER))
                .schemaRequirement(AUTH_HEADER, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name(AUTH_HEADER));
    }
}
