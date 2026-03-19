package org.mydotey.ai.site.portfolio.domain.repository;

import org.mydotey.ai.site.portfolio.domain.entity.ProjectLink;

import java.util.List;

/**
 * 项目链接仓储接口
 *
 * @author AI-Site
 */
public interface ProjectLinkRepository {

    /**
     * 根据项目ID查找链接
     */
    List<ProjectLink> findByProjectId(Long projectId);

    /**
     * 保存链接
     */
    void save(ProjectLink link);

    /**
     * 批量保存链接
     */
    void saveAll(List<ProjectLink> links);

    /**
     * 删除项目的所有链接
     */
    void deleteByProjectId(Long projectId);
}
