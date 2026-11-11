package com.blog.security;

/**
 * 线程级用户上下文
 *
 * 通过 ThreadLocal 存储当前请求的用户信息,
 * Controller / Service 层可随时读取, 不用层层传参。
 *
 * 生命周期: 拦截器 set -> Controller 使用 -> 请求结束 remove (防止内存泄漏!)
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();
    private static final ThreadLocal<String> ROLE = new ThreadLocal<>();

    public static void set(Long userId, String username, String role) {
        USER_ID.set(userId);
        USERNAME.set(username);
        ROLE.set(role);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static String getUsername() {
        return USERNAME.get();
    }

    public static String getRole() {
        return ROLE.get();
    }

    /**
     * 是否管理员
     */
    public static boolean isAdmin() {
        return "ADMIN".equals(ROLE.get());
    }

    /**
     * 请求结束必须清理! 否则线程池复用会导致数据串线
     */
    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
        ROLE.remove();
    }
}
