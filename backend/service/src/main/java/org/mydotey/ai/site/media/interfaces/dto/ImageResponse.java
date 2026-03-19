package org.mydotey.ai.site.media.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 图片响应
 *
 * @author AI-Site
 */
@Data
@Builder
public class ImageResponse {

    private Long id;
    private String title;
    private String description;
    private String originalName;
    private String url;
    private String thumbnailUrl;
    private Integer width;
    private Integer height;
    private Long size;
    private String mimeType;
    private Long albumId;
    private AlbumVO album;
    private Long folderId;
    private List<String> tags;
    private Integer isPublic;
    private Integer viewCount;
    private LocalDateTime createdAt;

    @Data
    @Builder
    public static class AlbumVO {
        private Long id;
        private String name;
        private String slug;
    }
}
