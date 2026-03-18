package org.mydotey.ai.site.blog.application.command;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Tag;
import org.mydotey.ai.site.blog.domain.repository.TagRepository;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 标签命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TagCommandService {

    private final TagRepository tagRepository;

    /**
     * 创建标签
     */
    public Long createTag(Tag tag) {
        // 检查 slug 是否已存在
        if (tagRepository.existsBySlug(tag.getSlug())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "标签别名已存在");
        }

        // 设置默认值
        if (tag.getColor() == null) {
            tag.setColor("#3b82f6");
        }
        if (tag.getArticleCount() == null) {
            tag.setArticleCount(0);
        }

        tagRepository.save(tag);
        return tag.getId();
    }

    /**
     * 更新标签
     */
    public void updateTag(Tag tag) {
        // 检查标签是否存在
        tagRepository.findById(tag.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "标签不存在"));

        // 检查 slug 是否已被其他标签使用
        if (tagRepository.existsBySlugExcludeId(tag.getSlug(), tag.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "标签别名已存在");
        }

        tagRepository.update(tag);
    }

    /**
     * 删除标签
     */
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "标签不存在"));

        tagRepository.deleteById(id);
    }
}
