package org.mydotey.ai.site.blog.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Article;
import org.mydotey.ai.site.blog.domain.entity.Category;
import org.mydotey.ai.site.blog.domain.entity.Tag;
import org.mydotey.ai.site.blog.domain.repository.ArticleRepository;
import org.mydotey.ai.site.blog.domain.repository.CategoryRepository;
import org.mydotey.ai.site.blog.domain.repository.TagRepository;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文章查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleQueryService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    /**
     * 分页查询文章（管理端）
     */
    public PageResult<Article> findPage(ArticleQuery query) {
        List<Article> articles = articleRepository.findPage(query);
        long total = articleRepository.count(query);

        // 填充分类和标签信息
        articles.forEach(this::fillArticleInfo);

        return PageResult.of(articles, total);
    }

    /**
     * 查询已发布的文章（前台）
     */
    public PageResult<Article> findPublished(int page, int size) {
        List<Article> articles = articleRepository.findPublished(page, size);
        articles.forEach(this::fillArticleInfo);

        // 简单估算总数
        long total = articles.size();
        if (articles.size() == size) {
            total = (long) page * size + size; // 估算
        }

        return PageResult.of(articles, total);
    }

    /**
     * 根据分类查询文章
     */
    public PageResult<Article> findByCategory(Long categoryId, int page, int size) {
        List<Article> articles = articleRepository.findByCategoryId(categoryId, page, size);
        articles.forEach(this::fillArticleInfo);

        long total = articles.size();
        if (articles.size() == size) {
            total = (long) page * size + size;
        }

        return PageResult.of(articles, total);
    }

    /**
     * 根据标签查询文章
     */
    public PageResult<Article> findByTag(Long tagId, int page, int size) {
        List<Article> articles = articleRepository.findByTagId(tagId, page, size);
        articles.forEach(this::fillArticleInfo);

        long total = articles.size();
        if (articles.size() == size) {
            total = (long) page * size + size;
        }

        return PageResult.of(articles, total);
    }

    /**
     * 搜索文章
     */
    public PageResult<Article> search(String keyword, int page, int size) {
        List<Article> articles = articleRepository.search(keyword, page, size);
        articles.forEach(this::fillArticleInfo);

        long total = articles.size();
        if (articles.size() == size) {
            total = (long) page * size + size;
        }

        return PageResult.of(articles, total);
    }

    /**
     * 根据ID查询文章
     */
    public Article findById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "文章不存在"));
        fillArticleInfo(article);
        return article;
    }

    /**
     * 根据Slug查询文章（前台）
     */
    public Article findBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "文章不存在"));

        // 只有已发布的文章才能被访问
        if (!article.isPublished()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }

        fillArticleInfo(article);

        // 增加浏览次数
        articleRepository.incrementViewCount(article.getId());
        article.setViewCount(article.getViewCount() + 1);

        return article;
    }

    /**
     * 获取相关文章
     */
    public List<Article> findRelated(Long articleId, int limit) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "文章不存在"));

        // 根据分类和标签查找相关文章
        ArticleQuery query = new ArticleQuery();
        query.setCategoryId(article.getCategoryId());
        query.setPage(1);
        query.setSize(limit + 1); // 多取一个，排除自己

        List<Article> related = articleRepository.findPage(query);

        // 排除当前文章
        related.removeIf(a -> a.getId().equals(articleId));

        // 限制数量
        if (related.size() > limit) {
            related = related.subList(0, limit);
        }

        related.forEach(this::fillArticleInfo);
        return related;
    }

    /**
     * 填充文章的分类和标签信息
     */
    private void fillArticleInfo(Article article) {
        if (article.getCategoryId() != null) {
            categoryRepository.findById(article.getCategoryId())
                    .ifPresent(article::setCategory);
        }

        List<Tag> tags = tagRepository.findByArticleId(article.getId());
        article.setTags(tags);
    }
}
