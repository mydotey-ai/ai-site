package org.mydotey.ai.site.blog.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Tag;
import org.mydotey.ai.site.blog.domain.repository.TagRepository;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 标签查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagQueryService {

    private final TagRepository tagRepository;

    /**
     * 查询所有标签
     */
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    /**
     * 根据ID查询标签
     */
    public Tag findById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "标签不存在"));
    }

    /**
     * 根据Slug查询标签
     */
    public Tag findBySlug(String slug) {
        return tagRepository.findBySlug(slug)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "标签不存在"));
    }

    /**
     * 根据文章ID查询标签
     */
    public List<Tag> findByArticleId(Long articleId) {
        return tagRepository.findByArticleId(articleId);
    }

    /**
     * 查询热门标签（按文章数量排序）
     */
    public List<Tag> findHotTags(int limit) {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .sorted((a, b) -> (b.getArticleCount() != null ? b.getArticleCount() : 0)
                        - (a.getArticleCount() != null ? a.getArticleCount() : 0))
                .limit(limit)
                .toList();
    }
}
