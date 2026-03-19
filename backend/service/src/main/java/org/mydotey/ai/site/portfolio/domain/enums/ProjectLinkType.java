package org.mydotey.ai.site.portfolio.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 项目链接类型枚举
 *
 * @author AI-Site
 */
@Getter
@RequiredArgsConstructor
public enum ProjectLinkType {

    /**
     * 演示地址
     */
    DEMO("DEMO", "演示地址"),

    /**
     * 源码地址
     */
    SOURCE("SOURCE", "源码地址"),

    /**
     * 文档地址
     */
    DOCS("DOCS", "文档地址"),

    /**
     * 其他链接
     */
    OTHER("OTHER", "其他链接");

    private final String code;
    private final String description;

    /**
     * 根据代码获取枚举
     */
    public static ProjectLinkType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ProjectLinkType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
