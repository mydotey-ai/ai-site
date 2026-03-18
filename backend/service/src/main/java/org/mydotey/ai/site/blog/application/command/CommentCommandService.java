package org.mydotey.ai.site.blog.application.command;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Article;
import org.mydotey.ai.site.blog.domain.entity.Comment;
import org.mydotey.ai.site.blog.domain.enums.CommentStatus;
import org.mydotey.ai.site.blog.domain.repository.ArticleRepository;
import org.mydotey.ai.site.blog.domain.repository.CommentRepository;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    /**
     * 提交评论
     */
    public Long submitComment(Comment comment) {
        // 验证文章是否存在
        Article article = articleRepository.findById(comment.getArticleId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "文章不存在"));

        // 检查文章是否允许评论
        if (article.getAllowComment() != null && article.getAllowComment() == 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该文章不允许评论");
        }

        // 如果是回复评论，验证父评论是否存在
        if (comment.getParentId() != null && comment.getParentId() > 0) {
            Comment parent = commentRepository.findById(comment.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "回复的评论不存在"));

            // 父评论必须是同一篇文章下的
            if (!parent.getArticleId().equals(comment.getArticleId())) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "无效的回复");
            }
        }

        // 设置默认值
        if (comment.getParentId() == null) {
            comment.setParentId(0L);
        }
        if (comment.getStatus() == null) {
            comment.setStatus(CommentStatus.PENDING.getCode());
        }
        if (comment.getLikeCount() == null) {
            comment.setLikeCount(0);
        }

        commentRepository.save(comment);
        return comment.getId();
    }

    /**
     * 审核通过评论
     */
    public void approveComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "评论不存在"));

        commentRepository.updateStatus(id, CommentStatus.APPROVED);
    }

    /**
     * 拒绝评论（标记为垃圾）
     */
    public void rejectComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "评论不存在"));

        commentRepository.updateStatus(id, CommentStatus.SPAM);
    }

    /**
     * 批量审核通过
     */
    public void batchApprove(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            commentRepository.batchUpdateStatus(ids, CommentStatus.APPROVED);
        }
    }

    /**
     * 批量拒绝
     */
    public void batchReject(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            commentRepository.batchUpdateStatus(ids, CommentStatus.SPAM);
        }
    }

    /**
     * 删除评论
     */
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "评论不存在"));

        commentRepository.deleteById(id);
    }

    /**
     * 点赞评论
     */
    public void likeComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "评论不存在"));

        if (!comment.isApproved()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该评论不可点赞");
        }

        commentRepository.incrementLikeCount(id);
    }
}
