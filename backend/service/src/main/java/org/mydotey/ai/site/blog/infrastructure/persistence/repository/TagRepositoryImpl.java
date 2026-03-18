package org.mydotey.ai.site.blog.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Tag;
import org.mydotey.ai.site.blog.domain.repository.TagRepository;
import org.mydotey.ai.site.blog.infrastructure.persistence.mapper.TagMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 标签仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagMapper tagMapper;

    @Override
    public Optional<Tag> findById(Long id) {
        return Optional.ofNullable(tagMapper.selectById(id));
    }

    @Override
    public Optional<Tag> findBySlug(String slug) {
        return Optional.ofNullable(tagMapper.findBySlug(slug));
    }

    @Override
    public boolean existsBySlug(String slug) {
        return tagMapper.countBySlug(slug) > 0;
    }

    @Override
    public boolean existsBySlugExcludeId(String slug, Long excludeId) {
        return tagMapper.countBySlugExcludeId(slug, excludeId) > 0;
    }

    @Override
    public void save(Tag tag) {
        tagMapper.insert(tag);
    }

    @Override
    public void update(Tag tag) {
        tagMapper.updateById(tag);
    }

    @Override
    public void deleteById(Long id) {
        tagMapper.deleteById(id);
    }

    @Override
    public List<Tag> findAll() {
        return tagMapper.findAllOrderByName();
    }

    @Override
    public List<Tag> findByArticleId(Long articleId) {
        return tagMapper.findByArticleId(articleId);
    }

    @Override
    public void updateArticleCount(Long id, int count) {
        tagMapper.updateArticleCount(id, count);
    }

    @Override
    public void batchUpdateArticleCount(List<Long> tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                // 查询实际文章数量并更新
                long count = tagMapper.findByArticleId(tagId).size();
                tagMapper.updateArticleCount(tagId, (int) count);
            }
        }
    }
}
