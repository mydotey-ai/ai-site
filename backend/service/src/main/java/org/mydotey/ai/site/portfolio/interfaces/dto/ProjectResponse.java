package org.mydotey.ai.site.portfolio.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目响应
 *
 * @author AI-Site
 */
@Data
@Builder
public class ProjectResponse {

    /**
     * 项目ID
     */
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * URL别名
     */
    private String slug;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目详情
     */
    private String content;

    /**
     * 封面图URL
     */
    private String coverImage;

    /**
     * 技术栈列表
     */
    private List<String> techStack;

    /**
     * 标签列表
     */
    private List<TagVO> tags;

    /**
     * 链接列表
     */
    private List<LinkVO> links;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 标签VO
     */
    @Data
    @Builder
    public static class TagVO {
        private Long id;
        private String name;
        private String slug;
        private String color;
    }

    /**
     * 链接VO
     */
    @Data
    @Builder
    public static class LinkVO {
        private Long id;
        private String type;
        private String label;
        private String url;
    }
}
