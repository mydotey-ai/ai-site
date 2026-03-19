package org.mydotey.ai.site.creation.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 章节请求DTO
 *
 * @author AI-Site
 */
@Data
public class ChapterRequest {

    /**
     * 小说ID
     */
    @NotNull(message = "小说ID不能为空")
    private Long novelId;

    /**
     * 章节标题
     */
    @NotBlank(message = "章节标题不能为空")
    @Size(max = 100, message = "章节标题最多100个字符")
    private String title;

    /**
     * 章节内容
     */
    private String content;

    /**
     * 章节序号
     */
    private Integer chapterNo;

    /**
     * 状态
     */
    private String status;
}
