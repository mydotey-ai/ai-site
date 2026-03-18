package org.mydotey.ai.site.blog.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Comment;
import org.mydotey.ai.site.blog.domain.enums.CommentStatus;
import org.mydotey.ai.site.blog.domain.repository.CommentRepository;
import org.mydotey.ai.site.blog.infrastructure.persistence.mapper.CommentMapper;
import org.mydotey.ai.site.blog.application.query.CommentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 评论仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentMapper commentMapper;

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(commentMapper.selectById(id));
    }

    @Override
    public void save(Comment comment) {
        commentMapper.insert(comment);
    }

    @Override
    public void update(Comment comment) {
        commentMapper.updateById(comment);
    }

    @Override
    public void deleteById(Long id) {
        commentMapper.deleteById(id);
    }

    @Override
    public List<Comment> findPage(CommentQuery query) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Comment::getCreatedAt);

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Comment::getStatus, query.getStatus());
        }
        if (query.getArticleId() != null) {
            wrapper.eq(Comment::getArticleId, query.getArticleId());
        }

        Page<Comment> page = new Page<>(query.getPage(), query.getSize());
        return commentMapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public long count(CommentQuery query) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Comment::getStatus, query.getStatus());
        }
        if (query.getArticleId() != null) {
            wrapper.eq(Comment::getArticleId, query.getArticleId());
        }

        return commentMapper.selectCount(wrapper);
    }

    @Override
    public List<Comment> findApprovedByArticleId(Long articleId) {
        return commentMapper.findApprovedByArticleId(articleId);
    }

    @Override
    public List<Comment> findByStatus(CommentStatus status, int page, int size) {
        Page<Comment> pageParam = new Page<>(page, size);
        return commentMapper.findByStatus(pageParam, status.getCode()).getRecords();
    }

    @Override
    public long countByArticleId(Long articleId) {
        return commentMapper.countByArticleId(articleId);
    }

    @Override
    public long countPending() {
        return commentMapper.countPending();
    }

    @Override
    public void updateStatus(Long id, CommentStatus status) {
        commentMapper.updateStatus(id, status.getCode());
    }

    @Override
    public void batchUpdateStatus(List<Long> ids, CommentStatus status) {
        if (ids != null && !ids.isEmpty()) {
            commentMapper.batchUpdateStatus(ids, status.getCode());
        }
    }

    @Override
    public void incrementLikeCount(Long id) {
        commentMapper.incrementLikeCount(id);
    }
}
