package org.mydotey.ai.site.creation.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.creation.application.query.NovelQuery;
import org.mydotey.ai.site.creation.domain.entity.Novel;
import org.mydotey.ai.site.creation.domain.repository.NovelRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.NovelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 小说仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class NovelRepositoryImpl implements NovelRepository {

    private final NovelMapper novelMapper;

    @Override
    public Optional<Novel> findById(Long id) {
        return Optional.ofNullable(novelMapper.selectById(id));
    }

    @Override
    public Optional<Novel> findBySlug(String slug) {
        return Optional.ofNullable(novelMapper.findBySlug(slug));
    }

    @Override
    public boolean existsBySlug(String slug) {
        return novelMapper.countBySlug(slug) > 0;
    }

    @Override
    public boolean existsBySlugExcludeId(String slug, Long excludeId) {
        return novelMapper.countBySlugExcludeId(slug, excludeId) > 0;
    }

    @Override
    public void save(Novel novel) {
        novelMapper.insert(novel);
    }

    @Override
    public void update(Novel novel) {
        novelMapper.updateById(novel);
    }

    @Override
    public void deleteById(Long id) {
        novelMapper.deleteById(id);
    }

    @Override
    public List<Novel> findPage(NovelQuery query) {
        LambdaQueryWrapper<Novel> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Novel::getCreatedAt);

        // 分类筛选
        if (query.getCategoryId() != null) {
            wrapper.eq(Novel::getCategoryId, query.getCategoryId());
        }

        // 状态筛选
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Novel::getStatus, query.getStatus());
        }

        // 关键词搜索
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                    .like(Novel::getTitle, query.getKeyword())
                    .or()
                    .like(Novel::getSummary, query.getKeyword()));
        }

        Page<Novel> page = new Page<>(query.getPage(), query.getSize());
        return novelMapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public long count(NovelQuery query) {
        LambdaQueryWrapper<Novel> wrapper = new LambdaQueryWrapper<>();

        if (query.getCategoryId() != null) {
            wrapper.eq(Novel::getCategoryId, query.getCategoryId());
        }

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Novel::getStatus, query.getStatus());
        }

        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                    .like(Novel::getTitle, query.getKeyword())
                    .or()
                    .like(Novel::getSummary, query.getKeyword()));
        }

        return novelMapper.selectCount(wrapper);
    }

    @Override
    public void updateStats(Long novelId) {
        novelMapper.updateStats(novelId);
    }
}
