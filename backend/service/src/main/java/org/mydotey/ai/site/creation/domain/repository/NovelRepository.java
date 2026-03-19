package org.mydotey.ai.site.creation.domain.repository;

import org.mydotey.ai.site.creation.application.query.NovelQuery;
import org.mydotey.ai.site.creation.domain.entity.Novel;

import java.util.List;
import java.util.Optional;

/**
 * 小说仓储接口
 *
 * @author AI-Site
 */
public interface NovelRepository {

    /**
     * 根据ID查找小说
     */
    Optional<Novel> findById(Long id);

    /**
     * 根据Slug查找小说
     */
    Optional<Novel> findBySlug(String slug);

    /**
     * 检查Slug是否存在
     */
    boolean existsBySlug(String slug);

    /**
     * 检查Slug是否存在（排除指定ID）
     */
    boolean existsBySlugExcludeId(String slug, Long excludeId);

    /**
     * 保存小说
     */
    void save(Novel novel);

    /**
     * 更新小说
     */
    void update(Novel novel);

    /**
     * 删除小说
     */
    void deleteById(Long id);

    /**
     * 分页查询小说
     */
    List<Novel> findPage(NovelQuery query);

    /**
     * 统计小说总数
     */
    long count(NovelQuery query);

    /**
     * 更新小说统计信息（字数、章节数）
     */
    void updateStats(Long novelId);
}
