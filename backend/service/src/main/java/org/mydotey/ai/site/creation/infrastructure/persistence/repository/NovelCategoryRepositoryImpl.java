package org.mydotey.ai.site.creation.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.creation.domain.entity.NovelCategory;
import org.mydotey.ai.site.creation.domain.repository.NovelCategoryRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.NovelCategoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 小说分类仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class NovelCategoryRepositoryImpl implements NovelCategoryRepository {

    private final NovelCategoryMapper novelCategoryMapper;

    @Override
    public Optional<NovelCategory> findById(Long id) {
        return Optional.ofNullable(novelCategoryMapper.selectById(id));
    }

    @Override
    public Optional<NovelCategory> findBySlug(String slug) {
        return Optional.ofNullable(novelCategoryMapper.findBySlug(slug));
    }

    @Override
    public List<NovelCategory> findAll() {
        return novelCategoryMapper.findAllOrderBySort();
    }

    @Override
    public void save(NovelCategory category) {
        novelCategoryMapper.insert(category);
    }

    @Override
    public void update(NovelCategory category) {
        novelCategoryMapper.updateById(category);
    }

    @Override
    public void deleteById(Long id) {
        novelCategoryMapper.deleteById(id);
    }
}
