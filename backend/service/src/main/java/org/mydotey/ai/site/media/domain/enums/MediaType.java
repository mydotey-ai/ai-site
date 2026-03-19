package org.mydotey.ai.site.media.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 媒体类型枚举
 *
 * @author AI-Site
 */
@Getter
@AllArgsConstructor
public enum MediaType {

    LOCAL("LOCAL", "本地上传"),
    EXTERNAL("EXTERNAL", "外链");

    private final String code;
    private final String description;

    public static MediaType fromCode(String code) {
        for (MediaType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
