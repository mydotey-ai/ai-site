package org.mydotey.ai.site.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举
 *
 * @author AI-Site
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 通用错误
    BAD_REQUEST(40000, "请求参数错误"),
    UNAUTHORIZED(40100, "未授权访问"),
    FORBIDDEN(40300, "禁止访问"),
    NOT_FOUND(40400, "资源不存在"),
    METHOD_NOT_ALLOWED(40500, "请求方法不允许"),
    ALREADY_EXISTS(40900, "资源已存在"),
    INTERNAL_ERROR(50000, "服务器内部错误"),

    // 认证相关 401xx
    LOGIN_FAILED(40101, "登录失败"),
    TOKEN_EXPIRED(40102, "Token已过期"),
    TOKEN_INVALID(40103, "Token无效"),
    USERNAME_EXISTS(40104, "用户名已存在"),
    EMAIL_EXISTS(40105, "邮箱已存在"),

    // 用户相关 402xx
    USER_NOT_FOUND(40201, "用户不存在"),
    USER_DISABLED(40202, "用户已被禁用"),

    // 文章相关 403xx
    ARTICLE_NOT_FOUND(40301, "文章不存在"),
    CATEGORY_NOT_FOUND(40302, "分类不存在"),
    DUPLICATE_SLUG(40303, "文章别名已存在"),

    // 作品相关 404xx
    PROJECT_NOT_FOUND(40401, "项目不存在"),

    // 创作相关 405xx
    NOVEL_NOT_FOUND(40501, "小说不存在"),
    CHAPTER_NOT_FOUND(40502, "章节不存在"),

    // 媒体相关 406xx
    IMAGE_NOT_FOUND(40601, "图片不存在"),
    ALBUM_NOT_FOUND(40602, "相册不存在"),
    UPLOAD_FAILED(40603, "文件上传失败"),
    VIDEO_NOT_FOUND(40604, "视频不存在"),
    AUDIO_NOT_FOUND(40605, "音频不存在"),
    FOLDER_NOT_FOUND(40606, "文件夹不存在"),
    FILE_TYPE_NOT_ALLOWED(40607, "文件类型不允许"),
    FILE_SIZE_EXCEEDED(40608, "文件大小超出限制"),
    INVALID_FILE_CONTENT(40609, "文件内容校验失败");

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误消息
     */
    private final String message;
}