package org.mydotey.ai.site.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.blog.domain.enums.ArticleStatus;
import org.mydotey.ai.site.blog.domain.enums.ContentType;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article")
public class Article extends BaseEntity {

    /**
     * 文章ID
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 状态: 0-草稿, 1-已发布, 2-隐藏
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
     * 分类（非数据库字段）
     */
    @TableField(exist = false)
    private Category category;

    /**
     * 标签列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<Tag> tags;

    /**
     * 获取文章状态枚举
     */
    public ArticleStatus getStatusEnum() {
        return ArticleStatus.fromCode(this.status);
    }

    /**
     * 获取内容类型枚举
     */
    public ContentType getContentTypeEnum() {
        return ContentType.fromCode(this.contentType);
    }

    /**
     * 是否已发布
     */
    public boolean isPublished() {
        return ArticleStatus.PUBLISHED.getCode().equals(this.status);
    }
}
