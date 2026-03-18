package org.mydotey.ai.site.blog.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Category;
import org.mydotey.ai.site.blog.domain.repository.CategoryRepository;
import org.mydotey.ai.site.blog.infrastructure.persistence.mapper.CategoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 分类仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryMapper categoryMapper;

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(categoryMapper.selectById(id));
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return Optional.ofNullable(categoryMapper.findBySlug(slug));
    }

    @Override
    public boolean existsBySlug(String slug) {
        return categoryMapper.countBySlug(slug) > 0;
    }

    @Override
    public boolean existsBySlugExcludeId(String slug, Long excludeId) {
        return categoryMapper.countBySlugExcludeId(slug, excludeId) > 0;
    }

    @Override
    public void save(Category category) {
        categoryMapper.insert(category);
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public List<Category> findAll() {
        return categoryMapper.findAllOrderBySortOrder();
    }

    @Override
    public List<Category> findByParentId(Long parentId) {
        return categoryMapper.findByParentId(parentId);
    }

    @Override
    public void updateArticleCount(Long id, int count) {
        categoryMapper.updateArticleCount(id, count);
    }
}
