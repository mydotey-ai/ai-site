package org.mydotey.ai.site.blog.domain.repository;

import org.mydotey.ai.site.blog.domain.entity.Comment;
import org.mydotey.ai.site.blog.domain.enums.CommentStatus;
import org.mydotey.ai.site.blog.application.query.CommentQuery;

import java.util.List;
import java.util.Optional;

/**
 * 评论仓储接口
 *
 * @author AI-Site
 */
public interface CommentRepository {

    /**
     * 根据ID查找评论
     */
    Optional<Comment> findById(Long id);

    /**
     * 保存评论
     */
    void save(Comment comment);

    /**
     * 更新评论
     */
    void update(Comment comment);

    /**
     * 删除评论
     */
    void deleteById(Long id);

    /**
     * 分页查询评论
     */
    List<Comment> findPage(CommentQuery query);

    /**
     * 统计评论总数
     */
    long count(CommentQuery query);

    /**
     * 根据文章ID查找已通过的评论
     */
    List<Comment> findApprovedByArticleId(Long articleId);

    /**
     * 根据状态查找评论
     */
    List<Comment> findByStatus(CommentStatus status, int page, int size);

    /**
     * 统计文章的评论数
     */
    long countByArticleId(Long articleId);

    /**
     * 统计待审核评论数
     */
    long countPending();

    /**
     * 更新状态
     */
    void updateStatus(Long id, CommentStatus status);

    /**
     * 批量更新状态
     */
    void batchUpdateStatus(List<Long> ids, CommentStatus status);

    /**
     * 增加点赞数
     */
    void incrementLikeCount(Long id);
}
