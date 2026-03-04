package org.mydotey.ai.site.common.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author AI-Site
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}