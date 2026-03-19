package org.mydotey.ai.site.auth.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户响应
 *
 * @author AI-Site
 */
@Data
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String avatar;
    private String bio;
    private Integer status;
    private List<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
