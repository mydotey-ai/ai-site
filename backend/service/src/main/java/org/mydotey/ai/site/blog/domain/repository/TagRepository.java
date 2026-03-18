package org.mydotey.ai.site.blog.domain.repository;

import org.mydotey.ai.site.blog.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * 标签仓储接口
 *
 * @author AI-Site
 */
public interface TagRepository {

    /**
     * 根据ID查找标签
     */
    Optional<Tag> findById(Long id);

    /**
     * 根据Slug查找标签
     */
    Optional<Tag> findBySlug(String slug);

    /**
     * 检查Slug是否存在
     */
    boolean existsBySlug(String slug);

    /**
     * 检查Slug是否存在（排除指定ID）
     */
    boolean existsBySlugExcludeId(String slug, Long excludeId);

    /**
     * 保存标签
     */
    void save(Tag tag);

    /**
     * 更新标签
     */
    void update(Tag tag);

    /**
     * 删除标签
     */
    void deleteById(Long id);

    /**
     * 查找所有标签
     */
    List<Tag> findAll();

    /**
     * 根据文章ID查找标签
     */
    List<Tag> findByArticleId(Long articleId);

    /**
     * 更新文章数量
     */
    void updateArticleCount(Long id, int count);

    /**
     * 批量更新文章数量
     */
    void batchUpdateArticleCount(List<Long> tagIds);
}
