package org.mydotey.ai.site.portfolio.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 项目链接请求
 *
 * @author AI-Site
 */
@Data
public class ProjectLinkRequest {

    /**
     * 链接类型: DEMO / SOURCE / DOCS / OTHER
     */
    @NotBlank(message = "链接类型不能为空")
    private String type;

    /**
     * 链接标签
     */
    @NotBlank(message = "链接标签不能为空")
    private String label;

    /**
     * 链接地址
     */
    @NotBlank(message = "链接地址不能为空")
    private String url;

    /**
     * 排序
     */
    private Integer sort;
}
