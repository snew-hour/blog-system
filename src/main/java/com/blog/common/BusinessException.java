package com.blog.common;

import lombok.Getter;

/**
 * 业务异常
 *
 * 所有业务层抛出的可预期异常都用这个类，
 * 由 GlobalExceptionHandler 统一捕获处理。
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }
}
