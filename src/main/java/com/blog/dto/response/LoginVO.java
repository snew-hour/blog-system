package com.blog.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/** 登录响应 */
@Data
@AllArgsConstructor
@Schema(description = "登录响应")
public class LoginVO {

    @Schema(description = "JWT Token (前端存到 localStorage, 每次请求放 Authorization Header)")
    private String token;

    @Schema(description = "Token 过期时间戳 (秒)")
    private Long expiresAt;

    @Schema(description = "用户信息")
    private UserVO user;
}
