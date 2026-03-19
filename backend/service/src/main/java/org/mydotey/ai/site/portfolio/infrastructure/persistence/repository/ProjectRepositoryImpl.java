package org.mydotey.ai.site.portfolio.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.portfolio.application.query.ProjectQuery;
import org.mydotey.ai.site.portfolio.domain.entity.Project;
import org.mydotey.ai.site.portfolio.domain.repository.ProjectRepository;
import org.mydotey.ai.site.portfolio.infrastructure.persistence.mapper.ProjectMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 项目仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectMapper projectMapper;

    @Override
    public Optional<Project> findById(Long id) {
        return Optional.ofNullable(projectMapper.selectById(id));
    }

    @Override
    public Optional<Project> findBySlug(String slug) {
        return Optional.ofNullable(projectMapper.findBySlug(slug));
    }

    @Override
    public boolean existsBySlug(String slug) {
        return projectMapper.countBySlug(slug) > 0;
    }

    @Override
    public boolean existsBySlugExcludeId(String slug, Long excludeId) {
        return projectMapper.countBySlugExcludeId(slug, excludeId) > 0;
    }

    @Override
    public void save(Project project) {
        projectMapper.insert(project);
    }

    @Override
    public void update(Project project) {
        projectMapper.updateById(project);
    }

    @Override
    public void deleteById(Long id) {
        projectMapper.deleteById(id);
    }

    @Override
    public List<Project> findPage(ProjectQuery query) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Project::getCreatedAt);

        // 状态筛选
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Project::getStatus, query.getStatus());
        }

        // 关键词搜索
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                    .like(Project::getName, query.getKeyword())
                    .or()
                    .like(Project::getDescription, query.getKeyword()));
        }

        // 标签筛选
        if (query.getTagId() != null) {
            Page<Project> page = new Page<>(query.getPage(), query.getSize());
            return projectMapper.findByTagId(page, query.getTagId()).getRecords();
        }

        Page<Project> page = new Page<>(query.getPage(), query.getSize());
        return projectMapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public long count(ProjectQuery query) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Project::getStatus, query.getStatus());
        }

        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                    .like(Project::getName, query.getKeyword())
                    .or()
                    .like(Project::getDescription, query.getKeyword()));
        }

        // 标签筛选需要特殊处理
        if (query.getTagId() != null) {
            // 这里简化处理，实际需要关联查询
            return projectMapper.findByTagId(new Page<>(1, Integer.MAX_VALUE), query.getTagId()).getTotal();
        }

        return projectMapper.selectCount(wrapper);
    }

    @Override
    public List<Long> findTagIdsByProjectId(Long projectId) {
        return projectMapper.findTagIdsByProjectId(projectId);
    }

    @Override
    public void saveProjectTags(Long projectId, List<Long> tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            projectMapper.insertProjectTags(projectId, tagIds);
        }
    }

    @Override
    public void deleteProjectTags(Long projectId) {
        projectMapper.deleteProjectTags(projectId);
    }
}
