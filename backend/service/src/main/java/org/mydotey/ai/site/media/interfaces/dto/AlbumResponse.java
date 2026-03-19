package org.mydotey.ai.site.media.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 相册响应
 *
 * @author AI-Site
 */
@Data
@Builder
public class AlbumResponse {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String coverImage;
    private Integer imageCount;
    private Integer isPublic;
    private Integer sort;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 相册中的图片（仅在详情中使用）
     */
    private List<ImageResponse> images;
}
