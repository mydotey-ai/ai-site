package org.mydotey.ai.site.blog.domain.repository;

import org.mydotey.ai.site.blog.domain.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * 分类仓储接口
 *
 * @author AI-Site
 */
public interface CategoryRepository {

    /**
     * 根据ID查找分类
     */
    Optional<Category> findById(Long id);

    /**
     * 根据Slug查找分类
     */
    Optional<Category> findBySlug(String slug);

    /**
     * 检查Slug是否存在
     */
    boolean existsBySlug(String slug);

    /**
     * 检查Slug是否存在（排除指定ID）
     */
    boolean existsBySlugExcludeId(String slug, Long excludeId);

    /**
     * 保存分类
     */
    void save(Category category);

    /**
     * 更新分类
     */
    void update(Category category);

    /**
     * 删除分类
     */
    void deleteById(Long id);

    /**
     * 查找所有分类
     */
    List<Category> findAll();

    /**
     * 查找子分类
     */
    List<Category> findByParentId(Long parentId);

    /**
     * 更新文章数量
     */
    void updateArticleCount(Long id, int count);
}
