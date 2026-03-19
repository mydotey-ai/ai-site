package org.mydotey.ai.site.creation.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 诗歌请求DTO
 *
 * @author AI-Site
 */
@Data
public class PoetryRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题最多100个字符")
    private String title;

    @Size(max = 100, message = "别名最多100个字符")
    private String slug;

    @Size(max = 50, message = "作者最多50个字符")
    private String author;

    private String content;

    private Long categoryId;

    private String status;
}
