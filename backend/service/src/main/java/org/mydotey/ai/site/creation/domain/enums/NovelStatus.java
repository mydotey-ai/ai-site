package org.mydotey.ai.site.creation.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 小说状态枚举
 *
 * @author AI-Site
 */
@Getter
@RequiredArgsConstructor
public enum NovelStatus {

    /**
     * 草稿
     */
    DRAFT("DRAFT", "草稿"),

    /**
     * 已发布（连载中）
     */
    PUBLISHED("PUBLISHED", "连载中"),

    /**
     * 已完结
     */
    COMPLETED("COMPLETED", "已完结");

    private final String code;
    private final String description;

    /**
     * 根据代码获取枚举
     */
    public static NovelStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (NovelStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
