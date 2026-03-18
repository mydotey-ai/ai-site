package org.mydotey.ai.site.blog.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 分类请求
 *
 * @author AI-Site
 */
@Data
public class CategoryRequest {

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称长度不能超过50字符")
    private String name;

    /**
     * URL别名
     */
    @NotBlank(message = "别名不能为空")
    @Size(max = 50, message = "别名长度不能超过50字符")
    private String slug;

    /**
     * 分类描述
     */
    @Size(max = 255, message = "描述长度不能超过255字符")
    private String description;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sortOrder;
}
