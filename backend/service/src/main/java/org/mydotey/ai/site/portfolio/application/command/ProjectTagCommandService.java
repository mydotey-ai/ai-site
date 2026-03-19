package org.mydotey.ai.site.portfolio.application.command;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectTag;
import org.mydotey.ai.site.portfolio.domain.repository.ProjectTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目标签命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectTagCommandService {

    private final ProjectTagRepository projectTagRepository;

    /**
     * 创建标签
     */
    public Long createTag(ProjectTag tag) {
        // 检查 slug 是否已存在
        if (tag.getSlug() != null && projectTagRepository.existsBySlug(tag.getSlug())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "标签别名已存在");
        }

        // 设置默认值
        if (tag.getSort() == null) {
            tag.setSort(0);
        }

        projectTagRepository.save(tag);
        return tag.getId();
    }

    /**
     * 更新标签
     */
    public void updateTag(ProjectTag tag) {
        // 检查标签是否存在
        projectTagRepository.findById(tag.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "标签不存在"));

        // 检查 slug 是否已被其他标签使用
        if (tag.getSlug() != null && projectTagRepository.existsBySlugExcludeId(tag.getSlug(), tag.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "标签别名已存在");
        }

        projectTagRepository.update(tag);
    }

    /**
     * 删除标签
     */
    public void deleteTag(Long id) {
        projectTagRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "标签不存在"));

        projectTagRepository.deleteById(id);
    }
}
