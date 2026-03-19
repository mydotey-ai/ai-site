package org.mydotey.ai.site.portfolio.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 项目标签请求
 *
 * @author AI-Site
 */
@Data
public class ProjectTagRequest {

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    @Size(max = 50, message = "标签名称长度不能超过50字符")
    private String name;

    /**
     * URL别名
     */
    @Size(max = 50, message = "别名长度不能超过50字符")
    private String slug;

    /**
     * 标签颜色(HEX)
     */
    @Size(max = 20, message = "颜色长度不能超过20字符")
    private String color;

    /**
     * 排序
     */
    private Integer sort;
}
