package org.mydotey.ai.site.blog.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 评论请求
 *
 * @author AI-Site
 */
@Data
public class CommentRequest {

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
    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50字符")
    private String nickname;

    /**
     * 邮箱
     */
    @Size(max = 100, message = "邮箱长度不能超过100字符")
    private String email;

    /**
     * 网站
     */
    @Size(max = 200, message = "网站长度不能超过200字符")
    private String website;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 1000, message = "评论内容长度不能超过1000字符")
    private String content;
}
