package org.mydotey.ai.site.portfolio.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.portfolio.domain.entity.Project;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectLink;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectTag;
import org.mydotey.ai.site.portfolio.domain.repository.ProjectLinkRepository;
import org.mydotey.ai.site.portfolio.domain.repository.ProjectRepository;
import org.mydotey.ai.site.portfolio.domain.repository.ProjectTagRepository;
import org.mydotey.ai.site.portfolio.domain.enums.ProjectStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectQueryService {

    private final ProjectRepository projectRepository;
    private final ProjectTagRepository projectTagRepository;
    private final ProjectLinkRepository projectLinkRepository;

    /**
     * 分页查询项目
     */
    public PageResult<Project> findPage(ProjectQuery query) {
        List<Project> projects = projectRepository.findPage(query);
        long total = projectRepository.count(query);

        // 加载标签和链接
        for (Project project : projects) {
            loadProjectRelations(project);
        }

        return PageResult.of(projects, total);
    }

    /**
     * 根据ID查询项目
     */
    public Project findById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("项目不存在"));
        loadProjectRelations(project);
        return project;
    }

    /**
     * 根据Slug查询项目
     */
    public Project findBySlug(String slug) {
        Project project = projectRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("项目不存在"));
        loadProjectRelations(project);
        return project;
    }

    /**
     * 查询已发布的项目
     */
    public PageResult<Project> findReleased(ProjectQuery query) {
        query.setStatus(ProjectStatus.RELEASED.getCode());
        return findPage(query);
    }

    /**
     * 加载项目关联数据（标签和链接）
     */
    private void loadProjectRelations(Project project) {
        // 加载标签
        List<ProjectTag> tags = projectTagRepository.findByProjectId(project.getId());
        project.setTags(tags);

        // 加载链接
        List<ProjectLink> links = projectLinkRepository.findByProjectId(project.getId());
        project.setLinks(links);
    }
}
