package org.mydotey.ai.site.creation.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 章节列表项DTO
 *
 * @author AI-Site
 */
@Data
@Builder
public class ChapterListItem {

    /**
     * 章节ID
     */
    private Long id;

    /**
     * 章节序号
     */
    private Integer chapterNo;

    /**
     * 章节标题
     */
    private String title;

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
     * 创建时间
     */
    private LocalDateTime createdAt;
}
