package org.mydotey.ai.site.portfolio.domain.repository;

import org.mydotey.ai.site.portfolio.application.query.ProjectQuery;
import org.mydotey.ai.site.portfolio.domain.entity.Project;

import java.util.List;
import java.util.Optional;

/**
 * 项目仓储接口
 *
 * @author AI-Site
 */
public interface ProjectRepository {

    /**
     * 根据ID查找项目
     */
    Optional<Project> findById(Long id);

    /**
     * 根据Slug查找项目
     */
    Optional<Project> findBySlug(String slug);

    /**
     * 检查Slug是否存在
     */
    boolean existsBySlug(String slug);

    /**
     * 检查Slug是否存在（排除指定ID）
     */
    boolean existsBySlugExcludeId(String slug, Long excludeId);

    /**
     * 保存项目
     */
    void save(Project project);

    /**
     * 更新项目
     */
    void update(Project project);

    /**
     * 删除项目
     */
    void deleteById(Long id);

    /**
     * 分页查询项目
     */
    List<Project> findPage(ProjectQuery query);

    /**
     * 统计项目总数
     */
    long count(ProjectQuery query);

    /**
     * 查找项目的标签ID列表
     */
    List<Long> findTagIdsByProjectId(Long projectId);

    /**
     * 保存项目标签关联
     */
    void saveProjectTags(Long projectId, List<Long> tagIds);

    /**
     * 删除项目标签关联
     */
    void deleteProjectTags(Long projectId);
}
