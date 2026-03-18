package org.mydotey.ai.site.blog.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Comment;
import org.mydotey.ai.site.blog.domain.enums.CommentStatus;
import org.mydotey.ai.site.blog.domain.repository.CommentRepository;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {

    private final CommentRepository commentRepository;

    /**
     * 分页查询评论（管理端）
     */
    public PageResult<Comment> findPage(CommentQuery query) {
        List<Comment> comments = commentRepository.findPage(query);
        long total = commentRepository.count(query);
        return PageResult.of(comments, total);
    }

    /**
     * 查询文章的评论（前台，树形结构）
     */
    public List<Comment> findByArticleId(Long articleId) {
        List<Comment> allComments = commentRepository.findApprovedByArticleId(articleId);

        // 构建树形结构
        Map<Long, List<Comment>> childrenMap = allComments.stream()
                .filter(c -> c.getParentId() != null && c.getParentId() > 0)
                .collect(Collectors.groupingBy(Comment::getParentId));

        return allComments.stream()
                .filter(Comment::isTopLevel)
                .peek(c -> setChildren(c, childrenMap))
                .collect(Collectors.toList());
    }

    /**
     * 查询待审核评论
     */
    public List<Comment> findPending(int page, int size) {
        return commentRepository.findByStatus(CommentStatus.PENDING, page, size);
    }

    /**
     * 统计待审核评论数
     */
    public long countPending() {
        return commentRepository.countPending();
    }

    /**
     * 根据ID查询评论
     */
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "评论不存在"));
    }

    /**
     * 递归设置子评论
     */
    private void setChildren(Comment comment, Map<Long, List<Comment>> childrenMap) {
        List<Comment> children = childrenMap.get(comment.getId());
        if (children != null) {
            comment.setChildren(children);
            children.forEach(c -> setChildren(c, childrenMap));
        }
    }
}
