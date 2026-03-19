package org.mydotey.ai.site.creation.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 散文响应DTO
 *
 * @author AI-Site
 */
@Data
@Builder
public class EssayResponse {

    private Long id;
    private String title;
    private String slug;
    private String author;
    private String summary;
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
