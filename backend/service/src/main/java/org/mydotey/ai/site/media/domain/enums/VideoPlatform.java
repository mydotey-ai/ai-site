package org.mydotey.ai.site.media.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 视频平台枚举
 *
 * @author AI-Site
 */
@Getter
@AllArgsConstructor
public enum VideoPlatform {

    LOCAL("LOCAL", "本地上传"),
    BILIBILI("BILIBILI", "哔哩哔哩"),
    YOUTUBE("YOUTUBE", "YouTube");

    private final String code;
    private final String description;

    public static VideoPlatform fromCode(String code) {
        for (VideoPlatform platform : values()) {
            if (platform.getCode().equals(code)) {
                return platform;
            }
        }
        return null;
    }
}
