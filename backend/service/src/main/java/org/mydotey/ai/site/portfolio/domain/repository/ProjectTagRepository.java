package org.mydotey.ai.site.portfolio.domain.repository;

import org.mydotey.ai.site.portfolio.domain.entity.ProjectTag;

import java.util.List;
import java.util.Optional;

/**
 * 项目标签仓储接口
 *
 * @author AI-Site
 */
public interface ProjectTagRepository {

    /**
     * 根据ID查找标签
     */
    Optional<ProjectTag> findById(Long id);

    /**
     * 根据Slug查找标签
     */
    Optional<ProjectTag> findBySlug(String slug);

    /**
     * 检查Slug是否存在
     */
    boolean existsBySlug(String slug);

    /**
     * 检查Slug是否存在（排除指定ID）
     */
    boolean existsBySlugExcludeId(String slug, Long excludeId);

    /**
     * 保存标签
     */
    void save(ProjectTag tag);

    /**
     * 更新标签
     */
    void update(ProjectTag tag);

    /**
     * 删除标签
     */
    void deleteById(Long id);

    /**
     * 查找所有标签
     */
    List<ProjectTag> findAll();

    /**
     * 根据项目ID查找标签
     */
    List<ProjectTag> findByProjectId(Long projectId);
}
