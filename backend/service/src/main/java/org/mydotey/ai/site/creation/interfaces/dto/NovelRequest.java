package org.mydotey.ai.site.creation.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 小说请求DTO
 *
 * @author AI-Site
 */
@Data
public class NovelRequest {

    /**
     * 小说标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题最多100个字符")
    private String title;

    /**
     * URL别名
     */
    @Size(max = 100, message = "别名最多100个字符")
    private String slug;

    /**
     * 作者
     */
    @Size(max = 50, message = "作者最多50个字符")
    private String author;

    /**
     * 简介
     */
    @Size(max = 1000, message = "简介最多1000个字符")
    private String summary;

    /**
     * 封面图URL
     */
    private String coverImage;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 状态
     */
    private String status;
}
