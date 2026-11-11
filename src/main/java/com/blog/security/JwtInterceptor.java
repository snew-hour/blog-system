package com.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.blog.common.ErrorCode;
import com.blog.common.Result;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器
 *
 * 职责:
 *  1. 从 Header 提取 Token
 *  2. 解析验证 Token
 *  3. 把用户信息写入 UserContext (ThreadLocal)
 *  4. 请求结束清理 UserContext
 *
 * 可选增强: 结合 Redis 实现 Token 黑名单 (登出时把 Token 放入 Redis)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从 Header 取 Token
        String token = request.getHeader("Authorization");
        if (token == null || token.isBlank()) {
            writeUnauthorized(response, "未携带 Token");
            return false;
        }

        // 2. 检查 Redis 黑名单 (登出的 Token)
        String blacklistKey = "token:blacklist:" + token;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey))) {
            writeUnauthorized(response, "Token 已失效 (已登出)");
            return false;
        }

        // 3. 解析
        Claims claims = jwtUtil.parseToken(token);
        if (claims == null) {
            writeUnauthorized(response, "Token 无效或已过期");
            return false;
        }

        // 4. 写入线程上下文
        Long userId = jwtUtil.getUserId(claims);
        String username = claims.getSubject();
        String role = jwtUtil.getRole(claims);
        UserContext.set(userId, username, role);

        log.debug("用户认证通过: userId={}, username={}, role={}", userId, username, role);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 必须清理 ThreadLocal, 否则线程池复用时数据串线!
        UserContext.clear();
    }

    /**
     * 返回 401 JSON (不用 Spring 的默认 401 HTML)
     */
    private void writeUnauthorized(HttpServletResponse response, String msg) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = Result.error(ErrorCode.UNAUTHORIZED.getCode(), msg);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
