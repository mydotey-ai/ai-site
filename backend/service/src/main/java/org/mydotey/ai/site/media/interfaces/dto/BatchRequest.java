package org.mydotey.ai.site.media.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量操作请求
 *
 * @author AI-Site
 */
@Data
public class BatchRequest {

    /**
     * 操作类型: move / delete / setPublic / setPrivate
     */
    @NotBlank(message = "操作类型不能为空")
    private String action;

    /**
     * 图片ID列表
     */
    @NotEmpty(message = "ID列表不能为空")
    private List<Long> ids;

    /**
     * 目标相册ID (move 操作需要)
     */
    private Long targetAlbumId;

    /**
     * 目标文件夹ID (move 操作需要)
     */
    private Long targetFolderId;
}
