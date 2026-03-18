package org.mydotey.ai.site.blog.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章响应
 *
 * @author AI-Site
 */
@Data
@Builder
public class ArticleResponse {

    /**
     * 文章ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * URL别名
     */
    private String slug;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 内容
     */
    private String content;

    /**
     * 内容类型
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
     * 分类信息
     */
    private CategoryVO category;

    /**
     * 标签列表
     */
    private List<TagVO> tags;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

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
    private String seoTitle;

    /**
     * SEO 描述
     */
    private String seoDescription;

    /**
     * SEO 关键词
     */
    private String seoKeywords;

    /**
     * 发布时间
     */
    private LocalDateTime publishedAt;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 分类VO
     */
    @Data
    @Builder
    public static class CategoryVO {
        private Long id;
        private String name;
        private String slug;
    }

    /**
     * 标签VO
     */
    @Data
    @Builder
    public static class TagVO {
        private Long id;
        private String name;
        private String slug;
        private String color;
    }
}
