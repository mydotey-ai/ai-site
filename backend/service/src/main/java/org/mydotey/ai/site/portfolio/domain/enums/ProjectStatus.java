package org.mydotey.ai.site.portfolio.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 项目状态枚举
 *
 * @author AI-Site
 */
@Getter
@RequiredArgsConstructor
public enum ProjectStatus {

    /**
     * 开发中
     */
    DEVELOPING("DEVELOPING", "开发中"),

    /**
     * 已发布
     */
    RELEASED("RELEASED", "已发布"),

    /**
     * 已归档
     */
    ARCHIVED("ARCHIVED", "已归档");

    private final String code;
    private final String description;

    /**
     * 根据代码获取枚举
     */
    public static ProjectStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ProjectStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
