package org.mydotey.ai.site.blog.application.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.PageQuery;

/**
 * 评论查询条件
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentQuery extends PageQuery {

    /**
     * 状态: PENDING, APPROVED, SPAM
     */
    private String status;

    /**
     * 文章ID
     */
    private Long articleId;
}
