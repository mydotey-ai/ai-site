package org.mydotey.ai.site.portfolio.application.command;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.portfolio.domain.entity.Project;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectLink;
import org.mydotey.ai.site.portfolio.domain.enums.ProjectStatus;
import org.mydotey.ai.site.portfolio.domain.repository.ProjectLinkRepository;
import org.mydotey.ai.site.portfolio.domain.repository.ProjectRepository;
import org.mydotey.ai.site.portfolio.domain.repository.ProjectTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectCommandService {

    private final ProjectRepository projectRepository;
    private final ProjectTagRepository projectTagRepository;
    private final ProjectLinkRepository projectLinkRepository;

    /**
     * 创建项目
     */
    public Long createProject(Project project, List<Long> tagIds, List<ProjectLink> links) {
        // 自动生成 slug
        if (StrUtil.isBlank(project.getSlug())) {
            project.setSlug(generateSlug(project.getName()));
        }

        // 检查 slug 是否已存在
        if (projectRepository.existsBySlug(project.getSlug())) {
            // 如果已存在，添加随机后缀
            project.setSlug(project.getSlug() + "-" + IdUtil.simpleUUID().substring(0, 6));
        }

        // 设置默认值
        if (project.getStatus() == null || project.getStatus().isEmpty()) {
            project.setStatus(ProjectStatus.DEVELOPING.getCode());
        }

        // 保存项目
        projectRepository.save(project);

        // 保存标签关联
        if (tagIds != null && !tagIds.isEmpty()) {
            // 验证标签是否存在
            for (Long tagId : tagIds) {
                projectTagRepository.findById(tagId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "标签不存在: " + tagId));
            }
            projectRepository.saveProjectTags(project.getId(), tagIds);
        }

        // 保存链接
        if (links != null && !links.isEmpty()) {
            for (int i = 0; i < links.size(); i++) {
                links.get(i).setProjectId(project.getId());
                links.get(i).setSort(i);
            }
            projectLinkRepository.saveAll(links);
        }

        return project.getId();
    }

    /**
     * 更新项目
     */
    public void updateProject(Project project, List<Long> tagIds, List<ProjectLink> links) {
        // 检查项目是否存在
        Project existing = projectRepository.findById(project.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "项目不存在"));

        // 检查 slug 是否已被其他项目使用
        if (project.getSlug() != null && projectRepository.existsBySlugExcludeId(project.getSlug(), project.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "项目别名已存在");
        }

        // 更新项目
        projectRepository.update(project);

        // 更新标签关联
        projectRepository.deleteProjectTags(project.getId());
        if (tagIds != null && !tagIds.isEmpty()) {
            projectRepository.saveProjectTags(project.getId(), tagIds);
        }

        // 更新链接
        projectLinkRepository.deleteByProjectId(project.getId());
        if (links != null && !links.isEmpty()) {
            for (int i = 0; i < links.size(); i++) {
                links.get(i).setProjectId(project.getId());
                links.get(i).setSort(i);
            }
            projectLinkRepository.saveAll(links);
        }
    }

    /**
     * 删除项目
     */
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "项目不存在"));

        // 删除标签关联
        projectRepository.deleteProjectTags(id);

        // 删除链接
        projectLinkRepository.deleteByProjectId(id);

        // 删除项目
        projectRepository.deleteById(id);
    }

    /**
     * 发布项目
     */
    public void releaseProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "项目不存在"));

        project.setStatus(ProjectStatus.RELEASED.getCode());
        projectRepository.update(project);
    }

    /**
     * 归档项目
     */
    public void archiveProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "项目不存在"));

        project.setStatus(ProjectStatus.ARCHIVED.getCode());
        projectRepository.update(project);
    }

    /**
     * 批量删除项目
     */
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            try {
                deleteProject(id);
            } catch (BusinessException e) {
                // 忽略不存在的项目
            }
        }
    }

    /**
     * 批量修改状态
     */
    public void batchUpdateStatus(List<Long> ids, String status) {
        for (Long id : ids) {
            Project project = projectRepository.findById(id).orElse(null);
            if (project != null) {
                project.setStatus(status);
                projectRepository.update(project);
            }
        }
    }

    /**
     * 生成 slug
     */
    private String generateSlug(String name) {
        if (StrUtil.isBlank(name)) {
            return "project-" + IdUtil.simpleUUID().substring(0, 8);
        }
        // 简单处理：转小写，替换空格和特殊字符
        String slug = name.toLowerCase()
                .replaceAll("[\\s\\p{Punct}&&[^-]]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        // 如果处理后的 slug 为空，使用随机字符串
        if (StrUtil.isBlank(slug)) {
            slug = "project-" + IdUtil.simpleUUID().substring(0, 8);
        }
        return slug;
    }
}
