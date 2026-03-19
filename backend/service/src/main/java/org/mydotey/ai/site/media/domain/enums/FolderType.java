package org.mydotey.ai.site.media.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件夹类型枚举
 *
 * @author AI-Site
 */
@Getter
@AllArgsConstructor
public enum FolderType {

    IMAGE("IMAGE", "图片"),
    VIDEO("VIDEO", "视频"),
    AUDIO("AUDIO", "音频");

    private final String code;
    private final String description;

    public static FolderType fromCode(String code) {
        for (FolderType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
