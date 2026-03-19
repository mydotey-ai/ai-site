package org.mydotey.ai.site.portfolio.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectTag;
import org.mydotey.ai.site.portfolio.domain.repository.ProjectTagRepository;
import org.mydotey.ai.site.portfolio.infrastructure.persistence.mapper.ProjectTagMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 项目标签仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class ProjectTagRepositoryImpl implements ProjectTagRepository {

    private final ProjectTagMapper projectTagMapper;

    @Override
    public Optional<ProjectTag> findById(Long id) {
        return Optional.ofNullable(projectTagMapper.selectById(id));
    }

    @Override
    public Optional<ProjectTag> findBySlug(String slug) {
        return Optional.ofNullable(projectTagMapper.findBySlug(slug));
    }

    @Override
    public boolean existsBySlug(String slug) {
        return projectTagMapper.countBySlug(slug) > 0;
    }

    @Override
    public boolean existsBySlugExcludeId(String slug, Long excludeId) {
        return projectTagMapper.countBySlugExcludeId(slug, excludeId) > 0;
    }

    @Override
    public void save(ProjectTag tag) {
        projectTagMapper.insert(tag);
    }

    @Override
    public void update(ProjectTag tag) {
        projectTagMapper.updateById(tag);
    }

    @Override
    public void deleteById(Long id) {
        projectTagMapper.deleteById(id);
    }

    @Override
    public List<ProjectTag> findAll() {
        return projectTagMapper.findAllOrderBySort();
    }

    @Override
    public List<ProjectTag> findByProjectId(Long projectId) {
        return projectTagMapper.findByProjectId(projectId);
    }
}
