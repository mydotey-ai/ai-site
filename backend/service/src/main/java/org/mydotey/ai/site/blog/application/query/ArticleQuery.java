package org.mydotey.ai.site.blog.application.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.PageQuery;

/**
 * 文章查询条件
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleQuery extends PageQuery {

    /**
     * 状态: 0-草稿, 1-已发布, 2-隐藏
     */
    private Integer status;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 作者ID
     */
    private Long authorId;
}
