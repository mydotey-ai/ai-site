package org.mydotey.ai.site.blog.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论响应
 *
 * @author AI-Site
 */
@Data
@Builder
public class CommentResponse {

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 网站
     */
    private String website;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 状态
     */
    private String status;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 子评论列表
     */
    private List<CommentResponse> children;
}
