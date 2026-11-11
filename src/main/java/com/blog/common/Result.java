package com.blog.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一 API 响应封装
 *
 * 所有 Controller 返回值都应该用这个类包装，
 * 保证前端拿到的响应结构一致: { code, message, data }
 */
@Data
public class Result<T> implements Serializable {

    private Integer code;       // 业务状态码: 200成功, 4xx客户端错误, 5xx服务端错误
    private String message;     // 提示信息
    private T data;             // 业务数据
    private Long timestamp;     // 时间戳

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // ========== 静态工厂方法 ==========

    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
}
