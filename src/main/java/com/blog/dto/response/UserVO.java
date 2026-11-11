package com.blog.dto.response;

import com.blog.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/** 用户信息 VO (脱敏后返回给前端) */
@Data
@Schema(description = "用户信息")
public class UserVO {

    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    /** 邮箱已脱敏, 如 a***@qq.com */
    private String email;
    private String role;
    private Integer status;
    private LocalDateTime lastLogin;
    private LocalDateTime createTime;

    /** Entity -> VO 转换 (脱敏邮箱) */
    public static UserVO from(User u) {
        if (u == null) return null;
        UserVO vo = new UserVO();
        vo.setId(u.getId());
        vo.setUsername(u.getUsername());
        vo.setNickname(u.getNickname());
        vo.setAvatar(u.getAvatar());
        vo.setEmail(maskEmail(u.getEmail()));
        vo.setRole(u.getRole());
        vo.setStatus(u.getStatus());
        vo.setLastLogin(u.getLastLogin());
        vo.setCreateTime(u.getCreateTime());
        return vo;
    }

    /** 邮箱脱敏: a****@qq.com */
    private static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        int idx = email.indexOf('@');
        if (idx <= 1) return email;
        return email.charAt(0) + "****" + email.substring(idx);
    }
}
