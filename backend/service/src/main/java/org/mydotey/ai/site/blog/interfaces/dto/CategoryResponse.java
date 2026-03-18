package org.mydotey.ai.site.blog.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类响应
 *
 * @author AI-Site
 */
@Data
@Builder
public class CategoryResponse {

    /**
     * 分类ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * URL别名
     */
    private String slug;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sortOrder;

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
