package org.mydotey.ai.site.blog.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 文章状态枚举
 *
 * @author AI-Site
 */
@Getter
@RequiredArgsConstructor
public enum ArticleStatus {

    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    HIDDEN(2, "隐藏");

    private final Integer code;
    private final String description;

    public static ArticleStatus fromCode(Integer code) {
        if (code == null) {
            return DRAFT;
        }
        for (ArticleStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return DRAFT;
    }
}
