package org.mydotey.ai.site.blog.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 内容类型枚举
 *
 * @author AI-Site
 */
@Getter
@RequiredArgsConstructor
public enum ContentType {

    MARKDOWN("MARKDOWN", "Markdown"),
    RICHTEXT("RICHTEXT", "富文本");

    private final String code;
    private final String description;

    public static ContentType fromCode(String code) {
        if (code == null) {
            return MARKDOWN;
        }
        for (ContentType type : values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return MARKDOWN;
    }
}
