package org.mydotey.ai.site.media.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 相册请求
 *
 * @author AI-Site
 */
@Data
public class AlbumRequest {

    @NotBlank(message = "相册名称不能为空")
    @Size(max = 100, message = "相册名称最多100个字符")
    private String name;

    private String slug;

    @Size(max = 500, message = "描述最多500个字符")
    private String description;

    private String coverImage;

    private Integer isPublic = 1;

    private Integer sort = 0;
}
