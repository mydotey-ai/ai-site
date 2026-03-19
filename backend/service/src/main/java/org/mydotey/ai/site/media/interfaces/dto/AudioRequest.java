package org.mydotey.ai.site.media.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 音频请求
 *
 * @author AI-Site
 */
@Data
public class AudioRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题最多100个字符")
    private String title;

    @Size(max = 500, message = "描述最多500个字符")
    private String description;

    private String coverImage;

    /**
     * 类型: LOCAL / EXTERNAL
     */
    @NotBlank(message = "类型不能为空")
    private String type;

    /**
     * 平台: NETEASE / LOCAL
     */
    private String platform;

    /**
     * 外链音频ID
     */
    private String audioId;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签列表
     */
    private List<String> tags;

    private Integer isPublic = 1;
}
