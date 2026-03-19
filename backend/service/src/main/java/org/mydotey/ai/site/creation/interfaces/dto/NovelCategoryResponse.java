package org.mydotey.ai.site.creation.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 小说分类响应DTO
 *
 * @author AI-Site
 */
@Data
@Builder
public class NovelCategoryResponse {

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
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
