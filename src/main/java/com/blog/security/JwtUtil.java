package com.blog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 *
 * 职责: 生成 / 解析 / 验证 Token
 * 算法: HS256
 *
 * HS256 要求密钥至少 256 位 (32 字节), 所以这里做了启动校验。
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private Long expireSeconds;

    @Value("${jwt.prefix:Bearer }")
    private String tokenPrefix;

    private SecretKey key;

    /**
     * 初始化密钥 (启动时校验密钥长度, 避免运行时异常)
     */
    @PostConstruct
    public void init() {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        // HS256 最低要求 32 字节 (256 bit)
        if (secretBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT secret 密钥长度不足! HS256 至少需要 32 字节 (256 bit), " +
                    "当前只有 " + secretBytes.length + " 字节。请修改 application.yml 中的 jwt.secret"
            );
        }
        this.key = Keys.hmacShaKeyFor(secretBytes);
        log.info("JWT 密钥初始化成功 ({} 字节)", secretBytes.length);
    }

    /**
     * 生成 Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param role     角色 (USER / ADMIN)
     */
    public String generateToken(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", userId);
        claims.put("sub", username);
        claims.put("role", role);

        long now = System.currentTimeMillis();
        long expireAt = now + expireSeconds * 1000;

        String token = Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(now))
                .expiration(new Date(expireAt))
                .signWith(key)
                .compact();

        return tokenPrefix + token;
    }

    /**
     * 解析 Token (返回 Claims, 失败返回 null)
     */
    public Claims parseToken(String token) {
        try {
            // 去掉 "Bearer " 前缀
            if (token.startsWith(tokenPrefix)) {
                token = token.substring(tokenPrefix.length());
            }
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.warn("Token 解析失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从 Claims 中取用户ID
     */
    public Long getUserId(Claims claims) {
        Object uid = claims.get("uid");
        return uid != null ? Long.valueOf(uid.toString()) : null;
    }

    /**
     * 从 Claims 中取角色
     */
    public String getRole(Claims claims) {
        Object role = claims.get("role");
        return role != null ? role.toString() : "USER";
    }

    /**
     * 纯 Token (不带前缀) 解析用
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }
}
