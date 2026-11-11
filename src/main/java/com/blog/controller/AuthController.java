package com.blog.controller;

import com.blog.common.Result;
import com.blog.dto.request.LoginRequest;
import com.blog.dto.request.RegisterRequest;
import com.blog.dto.response.LoginVO;
import com.blog.dto.response.UserVO;
import com.blog.security.UserContext;
import com.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器 (登录 / 注册 / 登出)
 *
 * 这两个接口在 WebMvcConfig 里配置了白名单, 不需要 Token
 */
@Tag(name = "认证", description = "登录 / 注册 / 登出")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterRequest req) {
        return Result.success("注册成功", userService.register(req));
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest req) {
        return Result.success("登录成功", userService.login(req));
    }

    @Operation(summary = "用户登出 (Token 加入黑名单)")
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        // 注意: 登出接口本身需要携带 Token (才能知道登出谁)
        String token = request.getHeader("Authorization");
        userService.logout(token);
        return Result.success("登出成功", null);
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/me")
    public Result<UserVO> me() {
        Long userId = UserContext.getUserId();
        return Result.success(userService.getById(userId));
    }
}
