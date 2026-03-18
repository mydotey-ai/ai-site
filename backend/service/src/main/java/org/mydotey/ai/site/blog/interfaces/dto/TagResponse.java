package org.mydotey.ai.site.blog.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签响应
 *
 * @author AI-Site
 */
@Data
@Builder
public class TagResponse {

    /**
     * 标签ID
     */
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * URL别名
     */
    private String slug;

    /**
     * 标签颜色
     */
    private String color;

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
