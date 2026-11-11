package com.blog.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局错误码枚举
 *
 * 规范:
 *   200     - 成功
 *   400-499 - 客户端错误 (参数/鉴权/权限)
 *   500-599 - 服务端错误 (业务异常/系统异常)
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ===== 通用 2xx/4xx =====
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或Token已过期"),
    FORBIDDEN(403, "没有访问权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),

    // ===== 业务错误 5xx =====
    SYSTEM_ERROR(500, "系统内部错误"),
    PARAM_VALIDATE_ERROR(501, "参数校验失败"),

    // ===== 用户模块 100x =====
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_ALREADY_EXIST(1002, "用户名已被占用"),
    PASSWORD_ERROR(1003, "密码错误"),
    USER_DISABLED(1004, "账号已被禁用"),
    OLD_PASSWORD_ERROR(1005, "原密码不正确"),

    // ===== 博客模块 200x =====
    ARTICLE_NOT_EXIST(2001, "文章不存在"),
    CATEGORY_NOT_EXIST(2002, "分类不存在"),
    COMMENT_NOT_EXIST(2003, "评论不存在");

    private final Integer code;
    private final String message;
}
