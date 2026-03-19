package org.mydotey.ai.site.creation.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.creation.domain.entity.PoetryCategory;
import org.mydotey.ai.site.creation.domain.repository.PoetryCategoryRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.PoetryCategoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 诗歌分类仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class PoetryCategoryRepositoryImpl implements PoetryCategoryRepository {

    private final PoetryCategoryMapper poetryCategoryMapper;

    @Override
    public Optional<PoetryCategory> findById(Long id) {
        return Optional.ofNullable(poetryCategoryMapper.selectById(id));
    }

    @Override
    public Optional<PoetryCategory> findBySlug(String slug) {
        return Optional.ofNullable(poetryCategoryMapper.findBySlug(slug));
    }

    @Override
    public List<PoetryCategory> findAll() {
        return poetryCategoryMapper.findAllOrderBySort();
    }

    @Override
    public void save(PoetryCategory category) {
        poetryCategoryMapper.insert(category);
    }

    @Override
    public void update(PoetryCategory category) {
        poetryCategoryMapper.updateById(category);
    }

    @Override
    public void deleteById(Long id) {
        poetryCategoryMapper.deleteById(id);
    }
}
