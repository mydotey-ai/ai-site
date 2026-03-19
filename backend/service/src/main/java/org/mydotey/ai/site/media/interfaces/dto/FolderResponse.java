package org.mydotey.ai.site.media.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件夹响应
 *
 * @author AI-Site
 */
@Data
@Builder
public class FolderResponse {

    private Long id;
    private String name;
    private Long parentId;
    private String type;
    private Integer sort;
    private LocalDateTime createdAt;

    /**
     * 子文件夹
     */
    private List<FolderResponse> children;
}
