package org.mydotey.ai.site.creation.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.creation.application.query.CreationQuery;
import org.mydotey.ai.site.creation.domain.entity.Essay;
import org.mydotey.ai.site.creation.domain.repository.EssayRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.EssayMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 散文仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class EssayRepositoryImpl implements EssayRepository {

    private final EssayMapper essayMapper;

    @Override
    public Optional<Essay> findById(Long id) {
        return Optional.ofNullable(essayMapper.selectById(id));
    }

    @Override
    public Optional<Essay> findBySlug(String slug) {
        return Optional.ofNullable(essayMapper.findBySlug(slug));
    }

    @Override
    public boolean existsBySlug(String slug) {
        return essayMapper.countBySlug(slug) > 0;
    }

    @Override
    public boolean existsBySlugExcludeId(String slug, Long excludeId) {
        return essayMapper.countBySlugExcludeId(slug, excludeId) > 0;
    }

    @Override
    public void save(Essay essay) {
        essayMapper.insert(essay);
    }

    @Override
    public void update(Essay essay) {
        essayMapper.updateById(essay);
    }

    @Override
    public void deleteById(Long id) {
        essayMapper.deleteById(id);
    }

    @Override
    public List<Essay> findPage(CreationQuery query) {
        LambdaQueryWrapper<Essay> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Essay::getCreatedAt);

        if (query.getCategoryId() != null) {
            wrapper.eq(Essay::getCategoryId, query.getCategoryId());
        }

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Essay::getStatus, query.getStatus());
        }

        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                    .like(Essay::getTitle, query.getKeyword())
                    .or()
                    .like(Essay::getSummary, query.getKeyword()));
        }

        Page<Essay> page = new Page<>(query.getPage(), query.getSize());
        return essayMapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public long count(CreationQuery query) {
        LambdaQueryWrapper<Essay> wrapper = new LambdaQueryWrapper<>();

        if (query.getCategoryId() != null) {
            wrapper.eq(Essay::getCategoryId, query.getCategoryId());
        }

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Essay::getStatus, query.getStatus());
        }

        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                    .like(Essay::getTitle, query.getKeyword())
                    .or()
                    .like(Essay::getSummary, query.getKeyword()));
        }

        return essayMapper.selectCount(wrapper);
    }
}
