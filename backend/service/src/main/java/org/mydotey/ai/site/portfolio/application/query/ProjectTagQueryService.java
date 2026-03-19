package org.mydotey.ai.site.portfolio.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectTag;
import org.mydotey.ai.site.portfolio.domain.repository.ProjectTagRepository;
import org.mydotey.ai.site.portfolio.infrastructure.persistence.mapper.ProjectTagMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目标签查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectTagQueryService {

    private final ProjectTagRepository projectTagRepository;
    private final ProjectTagMapper projectTagMapper;

    /**
     * 查询所有标签
     */
    public List<ProjectTag> findAll() {
        List<ProjectTag> tags = projectTagRepository.findAll();
        // 加载项目数量
        for (ProjectTag tag : tags) {
            int count = projectTagMapper.countProjectsByTagId(tag.getId());
            tag.setProjectCount(count);
        }
        return tags;
    }

    /**
     * 根据ID查询标签
     */
    public ProjectTag findById(Long id) {
        return projectTagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("标签不存在"));
    }
}
