package org.mydotey.ai.site.blog.domain.repository;

import org.mydotey.ai.site.blog.domain.entity.Article;
import org.mydotey.ai.site.blog.application.query.ArticleQuery;

import java.util.List;
import java.util.Optional;

/**
 * 文章仓储接口
 *
 * @author AI-Site
 */
public interface ArticleRepository {

    /**
     * 根据ID查找文章
     */
    Optional<Article> findById(Long id);

    /**
     * 根据Slug查找文章
     */
    Optional<Article> findBySlug(String slug);

    /**
     * 检查Slug是否存在
     */
    boolean existsBySlug(String slug);

    /**
     * 检查Slug是否存在（排除指定ID）
     */
    boolean existsBySlugExcludeId(String slug, Long excludeId);

    /**
     * 保存文章
     */
    void save(Article article);

    /**
     * 更新文章
     */
    void update(Article article);

    /**
     * 删除文章
     */
    void deleteById(Long id);

    /**
     * 分页查询文章
     */
    List<Article> findPage(ArticleQuery query);

    /**
     * 统计文章总数
     */
    long count(ArticleQuery query);

    /**
     * 查找已发布的文章
     */
    List<Article> findPublished(int page, int size);

    /**
     * 根据分类ID查找文章
     */
    List<Article> findByCategoryId(Long categoryId, int page, int size);

    /**
     * 根据标签ID查找文章
     */
    List<Article> findByTagId(Long tagId, int page, int size);

    /**
     * 搜索文章
     */
    List<Article> search(String keyword, int page, int size);

    /**
     * 增加浏览次数
     */
    void incrementViewCount(Long id);

    /**
     * 增加点赞数
     */
    void incrementLikeCount(Long id);

    /**
     * 查找文章的标签ID列表
     */
    List<Long> findTagIdsByArticleId(Long articleId);

    /**
     * 保存文章标签关联
     */
    void saveArticleTags(Long articleId, List<Long> tagIds);

    /**
     * 删除文章标签关联
     */
    void deleteArticleTags(Long articleId);
}
