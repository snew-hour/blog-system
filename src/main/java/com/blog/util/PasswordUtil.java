package com.blog.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密工具
 *
 * BCrypt 是目前业界推荐的密码哈希算法 (加盐 + 自适应成本因子),
 * 比 MD5 / SHA256 安全得多。
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /** 加密 */
    public static String encode(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    /** 验证 */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }
}
