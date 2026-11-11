package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.common.BusinessException;
import com.blog.common.ErrorCode;
import com.blog.dto.request.LoginRequest;
import com.blog.dto.request.RegisterRequest;
import com.blog.dto.response.LoginVO;
import com.blog.dto.response.UserVO;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.security.JwtUtil;
import com.blog.service.UserService;
import com.blog.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现
 *
 * 面试可讲的点:
 *  1. BCrypt 加密 (为什么不用 MD5? 加盐 + 慢哈希抗彩虹表)
 *  2. JWT 无状态 vs Session 有状态
 *  3. Redis Token 黑名单 (登出实现)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;

    @Value("${jwt.expire}")
    private Long expireSeconds;

    @Override
    public UserVO register(RegisterRequest req) {
        // 1. 检查用户名是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, req.getUsername())
        );
        if (count > 0) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);
        }

        // 2. 构建用户实体
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(PasswordUtil.encode(req.getPassword()));   // BCrypt 加密
        user.setNickname(req.getNickname() != null ? req.getNickname() : req.getUsername());
        user.setEmail(req.getEmail());
        user.setRole("USER");
        user.setStatus(1);

        // 3. 入库
        userMapper.insert(user);
        log.info("用户注册成功: id={}, username={}", user.getId(), user.getUsername());

        return UserVO.from(user);
    }

    @Override
    public LoginVO login(LoginRequest req) {
        // 1. 根据用户名查用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, req.getUsername())
        );
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST);
        }

        // 2. 验证密码
        if (!PasswordUtil.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 3. 检查状态
        if (user.getStatus() != 1) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }

        // 4. 生成 JWT
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        // 5. 更新最后登录时间
        user.setLastLogin(LocalDateTime.now());
        userMapper.updateById(user);

        // 6. 返回
        long expiresAt = System.currentTimeMillis() / 1000 + expireSeconds;
        return new LoginVO(token, expiresAt, UserVO.from(user));
    }

    @Override
    public UserVO getById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST);
        }
        return UserVO.from(user);
    }

    @Override
    public void logout(String token) {
        // 去掉前缀后存入黑名单, 过期时间和 Token 保持一致
        if (token != null && token.startsWith(jwtUtil.getTokenPrefix())) {
            String realToken = token.substring(jwtUtil.getTokenPrefix().length());
            redisTemplate.opsForValue().set(
                    "token:blacklist:" + realToken,
                    "1",
                    expireSeconds,
                    TimeUnit.SECONDS
            );
            log.info("用户登出, Token 已加入黑名单");
        }
    }
}
