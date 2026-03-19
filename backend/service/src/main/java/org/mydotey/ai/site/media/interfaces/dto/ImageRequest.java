package org.mydotey.ai.site.media.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 图片请求
 *
 * @author AI-Site
 */
@Data
public class ImageRequest {

    @Size(max = 100, message = "标题最多100个字符")
    private String title;

    @Size(max = 500, message = "描述最多500个字符")
    private String description;

    private Long albumId;

    private Long folderId;

    private List<String> tags;

    private Integer isPublic = 1;
}
