package org.mydotey.ai.site.portfolio.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectLink;
import org.mydotey.ai.site.portfolio.domain.repository.ProjectLinkRepository;
import org.mydotey.ai.site.portfolio.infrastructure.persistence.mapper.ProjectLinkMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目链接仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class ProjectLinkRepositoryImpl implements ProjectLinkRepository {

    private final ProjectLinkMapper projectLinkMapper;

    @Override
    public List<ProjectLink> findByProjectId(Long projectId) {
        return projectLinkMapper.findByProjectId(projectId);
    }

    @Override
    public void save(ProjectLink link) {
        projectLinkMapper.insert(link);
    }

    @Override
    public void saveAll(List<ProjectLink> links) {
        if (links != null && !links.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            links.forEach(link -> {
                if (link.getCreatedAt() == null) {
                    link.setCreatedAt(now);
                }
            });
            projectLinkMapper.insertAll(links);
        }
    }

    @Override
    public void deleteByProjectId(Long projectId) {
        projectLinkMapper.deleteByProjectId(projectId);
    }
}
