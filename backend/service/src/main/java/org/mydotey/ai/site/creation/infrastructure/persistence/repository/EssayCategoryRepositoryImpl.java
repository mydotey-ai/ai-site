package org.mydotey.ai.site.creation.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.creation.domain.entity.EssayCategory;
import org.mydotey.ai.site.creation.domain.repository.EssayCategoryRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.EssayCategoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 散文分类仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class EssayCategoryRepositoryImpl implements EssayCategoryRepository {

    private final EssayCategoryMapper essayCategoryMapper;

    @Override
    public Optional<EssayCategory> findById(Long id) {
        return Optional.ofNullable(essayCategoryMapper.selectById(id));
    }

    @Override
    public Optional<EssayCategory> findBySlug(String slug) {
        return Optional.ofNullable(essayCategoryMapper.findBySlug(slug));
    }

    @Override
    public List<EssayCategory> findAll() {
        return essayCategoryMapper.findAllOrderBySort();
    }

    @Override
    public void save(EssayCategory category) {
        essayCategoryMapper.insert(category);
    }

    @Override
    public void update(EssayCategory category) {
        essayCategoryMapper.updateById(category);
    }

    @Override
    public void deleteById(Long id) {
        essayCategoryMapper.deleteById(id);
    }
}
