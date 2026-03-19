package org.mydotey.ai.site.creation.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 小说响应DTO
 *
 * @author AI-Site
 */
@Data
@Builder
public class NovelResponse {

    /**
     * 小说ID
     */
    private Long id;

    /**
     * 小说标题
     */
    private String title;

    /**
     * URL别名
     */
    private String slug;

    /**
     * 作者
     */
    private String author;

    /**
     * 简介
     */
    private String summary;

    /**
     * 封面图URL
     */
    private String coverImage;

    /**
     * 分类
     */
    private CategoryVO category;

    /**
     * 状态
     */
    private String status;

    /**
     * 总字数
     */
    private Integer wordCount;

    /**
     * 章节数
     */
    private Integer chapterCount;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 分类VO
     */
    @Data
    @Builder
    public static class CategoryVO {
        private Long id;
        private String name;
        private String slug;
    }
}
