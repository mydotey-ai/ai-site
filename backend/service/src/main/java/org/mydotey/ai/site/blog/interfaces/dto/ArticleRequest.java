package org.mydotey.ai.site.blog.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 文章请求
 *
 * @author AI-Site
 */
@Data
public class ArticleRequest {

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200字符")
    private String title;

    /**
     * URL别名
     */
    @NotBlank(message = "别名不能为空")
    @Size(max = 200, message = "别名长度不能超过200字符")
    private String slug;

    /**
     * 摘要
     */
    @Size(max = 500, message = "摘要长度不能超过500字符")
    private String summary;

    /**
     * 内容
     */
    private String content;

    /**
     * 内容类型: MARKDOWN, RICHTEXT
     */
    private String contentType;

    /**
     * 封面图片
     */
    private String coverImage;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 标签ID列表
     */
    private List<Long> tagIds;

    /**
     * 状态: 0-草稿, 1-已发布, 2-隐藏
     */
    private Integer status;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否允许评论
     */
    private Integer allowComment;

    /**
     * SEO 标题
     */
    @Size(max = 100, message = "SEO标题长度不能超过100字符")
    private String seoTitle;

    /**
     * SEO 描述
     */
    @Size(max = 200, message = "SEO描述长度不能超过200字符")
    private String seoDescription;

    /**
     * SEO 关键词
     */
    @Size(max = 200, message = "SEO关键词长度不能超过200字符")
    private String seoKeywords;
}
