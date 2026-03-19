package org.mydotey.ai.site.portfolio.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 项目请求
 *
 * @author AI-Site
 */
@Data
public class ProjectRequest {

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 100, message = "项目名称长度不能超过100字符")
    private String name;

    /**
     * URL别名
     */
    @Size(max = 100, message = "别名长度不能超过100字符")
    private String slug;

    /**
     * 项目描述
     */
    @Size(max = 500, message = "描述长度不能超过500字符")
    private String description;

    /**
     * 项目详情(Markdown)
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
     * 标签ID列表
     */
    private List<Long> tagIds;

    /**
     * 项目链接列表
     */
    private List<LinkRequest> links;

    /**
     * 状态: DEVELOPING / RELEASED / ARCHIVED
     */
    private String status;

    /**
     * 链接请求
     */
    @Data
    public static class LinkRequest {
        /**
         * 链接类型: DEMO / SOURCE / DOCS / OTHER
         */
        private String type;

        /**
         * 链接标签
         */
        private String label;

        /**
         * 链接地址
         */
        private String url;
    }
}
