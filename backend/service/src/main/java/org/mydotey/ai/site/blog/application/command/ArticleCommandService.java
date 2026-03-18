package org.mydotey.ai.site.blog.application.command;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Article;
import org.mydotey.ai.site.blog.domain.enums.ArticleStatus;
import org.mydotey.ai.site.blog.domain.repository.ArticleRepository;
import org.mydotey.ai.site.blog.domain.repository.CategoryRepository;
import org.mydotey.ai.site.blog.domain.repository.TagRepository;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommandService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    /**
     * 创建文章
     */
    public Long createArticle(Article article, List<Long> tagIds) {
        // 验证分类是否存在
        if (article.getCategoryId() != null) {
            categoryRepository.findById(article.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));
        }

        // 检查 slug 是否已存在
        if (articleRepository.existsBySlug(article.getSlug())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "文章别名已存在");
        }

        // 设置默认值
        if (article.getStatus() == null) {
            article.setStatus(ArticleStatus.DRAFT.getCode());
        }
        if (article.getViewCount() == null) {
            article.setViewCount(0);
        }
        if (article.getLikeCount() == null) {
            article.setLikeCount(0);
        }
        if (article.getIsTop() == null) {
            article.setIsTop(0);
        }
        if (article.getAllowComment() == null) {
            article.setAllowComment(1);
        }
        if (article.getContentType() == null) {
            article.setContentType("MARKDOWN");
        }

        // 如果发布，设置发布时间
        if (ArticleStatus.PUBLISHED.getCode().equals(article.getStatus())) {
            article.setPublishedAt(LocalDateTime.now());
        }

        // 保存文章
        articleRepository.save(article);

        // 保存标签关联
        if (tagIds != null && !tagIds.isEmpty()) {
            // 验证标签是否存在
            for (Long tagId : tagIds) {
                tagRepository.findById(tagId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "标签不存在: " + tagId));
            }
            articleRepository.saveArticleTags(article.getId(), tagIds);
        }

        return article.getId();
    }

    /**
     * 更新文章
     */
    public void updateArticle(Article article, List<Long> tagIds) {
        // 检查文章是否存在
        Article existing = articleRepository.findById(article.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "文章不存在"));

        // 验证分类是否存在
        if (article.getCategoryId() != null) {
            categoryRepository.findById(article.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));
        }

        // 检查 slug 是否已被其他文章使用
        if (articleRepository.existsBySlugExcludeId(article.getSlug(), article.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "文章别名已存在");
        }

        // 如果从草稿变为发布，设置发布时间
        if (!ArticleStatus.PUBLISHED.getCode().equals(existing.getStatus())
                && ArticleStatus.PUBLISHED.getCode().equals(article.getStatus())) {
            article.setPublishedAt(LocalDateTime.now());
        }

        // 更新文章
        articleRepository.update(article);

        // 更新标签关联
        articleRepository.deleteArticleTags(article.getId());
        if (tagIds != null && !tagIds.isEmpty()) {
            articleRepository.saveArticleTags(article.getId(), tagIds);
        }
    }

    /**
     * 删除文章
     */
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "文章不存在"));

        // 删除标签关联
        articleRepository.deleteArticleTags(id);

        // 删除文章
        articleRepository.deleteById(id);
    }

    /**
     * 发布文章
     */
    public void publishArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "文章不存在"));

        article.setStatus(ArticleStatus.PUBLISHED.getCode());
        article.setPublishedAt(LocalDateTime.now());
        articleRepository.update(article);
    }

    /**
     * 取消发布文章
     */
    public void unpublishArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "文章不存在"));

        article.setStatus(ArticleStatus.DRAFT.getCode());
        articleRepository.update(article);
    }

    /**
     * 置顶文章
     */
    public void topArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "文章不存在"));

        article.setIsTop(1);
        articleRepository.update(article);
    }

    /**
     * 取消置顶
     */
    public void untopArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "文章不存在"));

        article.setIsTop(0);
        articleRepository.update(article);
    }
}
