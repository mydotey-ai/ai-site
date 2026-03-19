package org.mydotey.ai.site.media.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 音频响应
 *
 * @author AI-Site
 */
@Data
@Builder
public class AudioResponse {

    private Long id;
    private String title;
    private String description;
    private String coverImage;
    private String type;
    private String platform;
    private String audioId;
    private String url;
    private Integer duration;
    private Long size;
    private String category;
    private List<String> tags;
    private Integer isPublic;
    private Integer viewCount;
    private LocalDateTime createdAt;
}
