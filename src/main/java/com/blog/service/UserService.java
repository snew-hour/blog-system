package com.blog.service;

import com.blog.dto.request.LoginRequest;
import com.blog.dto.request.RegisterRequest;
import com.blog.dto.response.LoginVO;
import com.blog.dto.response.UserVO;

public interface UserService {

    /** 用户注册 */
    UserVO register(RegisterRequest req);

    /** 用户登录 (返回 JWT Token) */
    LoginVO login(LoginRequest req);

    /** 根据 ID 查用户 */
    UserVO getById(Long userId);

    /** 登出 (Token 加入 Redis 黑名单) */
    void logout(String token);
}
