package org.mydotey.ai.site.creation.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.creation.application.query.CreationQuery;
import org.mydotey.ai.site.creation.domain.entity.Poetry;
import org.mydotey.ai.site.creation.domain.repository.PoetryRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.PoetryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 诗歌仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class PoetryRepositoryImpl implements PoetryRepository {

    private final PoetryMapper poetryMapper;

    @Override
    public Optional<Poetry> findById(Long id) {
        return Optional.ofNullable(poetryMapper.selectById(id));
    }

    @Override
    public Optional<Poetry> findBySlug(String slug) {
        return Optional.ofNullable(poetryMapper.findBySlug(slug));
    }

    @Override
    public boolean existsBySlug(String slug) {
        return poetryMapper.countBySlug(slug) > 0;
    }

    @Override
    public boolean existsBySlugExcludeId(String slug, Long excludeId) {
        return poetryMapper.countBySlugExcludeId(slug, excludeId) > 0;
    }

    @Override
    public void save(Poetry poetry) {
        poetryMapper.insert(poetry);
    }

    @Override
    public void update(Poetry poetry) {
        poetryMapper.updateById(poetry);
    }

    @Override
    public void deleteById(Long id) {
        poetryMapper.deleteById(id);
    }

    @Override
    public List<Poetry> findPage(CreationQuery query) {
        LambdaQueryWrapper<Poetry> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Poetry::getCreatedAt);

        if (query.getCategoryId() != null) {
            wrapper.eq(Poetry::getCategoryId, query.getCategoryId());
        }

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Poetry::getStatus, query.getStatus());
        }

        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                    .like(Poetry::getTitle, query.getKeyword())
                    .or()
                    .like(Poetry::getContent, query.getKeyword()));
        }

        Page<Poetry> page = new Page<>(query.getPage(), query.getSize());
        return poetryMapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public long count(CreationQuery query) {
        LambdaQueryWrapper<Poetry> wrapper = new LambdaQueryWrapper<>();

        if (query.getCategoryId() != null) {
            wrapper.eq(Poetry::getCategoryId, query.getCategoryId());
        }

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Poetry::getStatus, query.getStatus());
        }

        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                    .like(Poetry::getTitle, query.getKeyword())
                    .or()
                    .like(Poetry::getContent, query.getKeyword()));
        }

        return poetryMapper.selectCount(wrapper);
    }
}
