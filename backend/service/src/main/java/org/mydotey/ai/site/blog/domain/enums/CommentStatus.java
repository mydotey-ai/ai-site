package org.mydotey.ai.site.blog.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 评论状态枚举
 *
 * @author AI-Site
 */
@Getter
@RequiredArgsConstructor
public enum CommentStatus {

    PENDING("PENDING", "待审核"),
    APPROVED("APPROVED", "已通过"),
    SPAM("SPAM", "垃圾");

    private final String code;
    private final String description;

    public static CommentStatus fromCode(String code) {
        if (code == null) {
            return PENDING;
        }
        for (CommentStatus status : values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        return PENDING;
    }
}
