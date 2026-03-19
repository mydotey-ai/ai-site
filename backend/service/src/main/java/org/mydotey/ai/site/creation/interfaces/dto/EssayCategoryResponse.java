package org.mydotey.ai.site.creation.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 散文分类响应DTO
 *
 * @author AI-Site
 */
@Data
@Builder
public class EssayCategoryResponse {

    private Long id;
    private String name;
    private String slug;
    private Integer sort;
    private LocalDateTime createdAt;
}
