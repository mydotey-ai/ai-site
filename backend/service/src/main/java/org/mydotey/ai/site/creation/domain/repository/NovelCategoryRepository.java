package org.mydotey.ai.site.creation.domain.repository;

import org.mydotey.ai.site.creation.domain.entity.NovelCategory;

import java.util.List;
import java.util.Optional;

/**
 * 小说分类仓储接口
 *
 * @author AI-Site
 */
public interface NovelCategoryRepository {

    /**
     * 根据ID查找分类
     */
    Optional<NovelCategory> findById(Long id);

    /**
     * 根据Slug查找分类
     */
    Optional<NovelCategory> findBySlug(String slug);

    /**
     * 查找所有分类
     */
    List<NovelCategory> findAll();

    /**
     * 保存分类
     */
    void save(NovelCategory category);

    /**
     * 更新分类
     */
    void update(NovelCategory category);

    /**
     * 删除分类
     */
    void deleteById(Long id);
}
