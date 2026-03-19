package org.mydotey.ai.site.media.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图片上传响应
 *
 * @author AI-Site
 */
@Data
@Builder
public class ImageUploadResponse {

    private Long id;
    private String title;
    private String url;
    private String thumbnailUrl;
    private Integer width;
    private Integer height;
    private Long size;
    private LocalDateTime createdAt;
}
