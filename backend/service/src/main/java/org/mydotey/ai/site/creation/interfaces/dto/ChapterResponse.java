package org.mydotey.ai.site.creation.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 章节响应DTO
 *
 * @author AI-Site
 */
@Data
@Builder
public class ChapterResponse {

    /**
     * 章节ID
     */
    private Long id;

    /**
     * 小说ID
     */
    private Long novelId;

    /**
     * 小说标题
     */
    private String novelTitle;

    /**
     * 章节序号
     */
    private Integer chapterNo;

    /**
     * 章节标题
     */
    private String title;

    /**
     * 章节内容
     */
    private String content;

    /**
     * 字数
     */
    private Integer wordCount;

    /**
     * 状态
     */
    private String status;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 上一章ID
     */
    private Long prevChapterId;

    /**
     * 下一章ID
     */
    private Long nextChapterId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
