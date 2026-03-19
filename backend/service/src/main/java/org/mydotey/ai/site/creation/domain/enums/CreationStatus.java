package org.mydotey.ai.site.creation.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 创作状态枚举（诗歌、散文通用）
 *
 * @author AI-Site
 */
@Getter
@RequiredArgsConstructor
public enum CreationStatus {

    /**
     * 草稿
     */
    DRAFT("DRAFT", "草稿"),

    /**
     * 已发布
     */
    PUBLISHED("PUBLISHED", "已发布");

    private final String code;
    private final String description;

    /**
     * 根据代码获取枚举
     */
    public static CreationStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (CreationStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
