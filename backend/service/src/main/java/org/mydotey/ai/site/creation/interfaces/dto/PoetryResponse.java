package org.mydotey.ai.site.creation.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 诗歌响应DTO
 *
 * @author AI-Site
 */
@Data
@Builder
public class PoetryResponse {

    private Long id;
    private String title;
    private String slug;
    private String author;
    private String content;
    private CategoryVO category;
    private String status;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    public static class CategoryVO {
        private Long id;
        private String name;
        private String slug;
    }
}
