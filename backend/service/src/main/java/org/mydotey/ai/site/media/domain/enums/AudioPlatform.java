package org.mydotey.ai.site.media.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 音频平台枚举
 *
 * @author AI-Site
 */
@Getter
@AllArgsConstructor
public enum AudioPlatform {

    LOCAL("LOCAL", "本地上传"),
    NETEASE("NETEASE", "网易云音乐");

    private final String code;
    private final String description;

    public static AudioPlatform fromCode(String code) {
        for (AudioPlatform platform : values()) {
            if (platform.getCode().equals(code)) {
                return platform;
            }
        }
        return null;
    }
}
