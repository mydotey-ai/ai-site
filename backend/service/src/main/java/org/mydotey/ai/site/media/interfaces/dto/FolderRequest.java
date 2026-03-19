package org.mydotey.ai.site.media.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 文件夹请求
 *
 * @author AI-Site
 */
@Data
public class FolderRequest {

    @NotBlank(message = "文件夹名称不能为空")
    @Size(max = 100, message = "文件夹名称最多100个字符")
    private String name;

    private Long parentId;

    @NotBlank(message = "类型不能为空")
    private String type;

    private Integer sort = 0;
}
