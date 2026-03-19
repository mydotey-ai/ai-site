package org.mydotey.ai.site.creation.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.creation.domain.entity.Chapter;
import org.mydotey.ai.site.creation.domain.entity.Novel;
import org.mydotey.ai.site.creation.domain.entity.NovelCategory;
import org.mydotey.ai.site.creation.domain.enums.NovelStatus;
import org.mydotey.ai.site.creation.domain.repository.ChapterRepository;
import org.mydotey.ai.site.creation.domain.repository.NovelCategoryRepository;
import org.mydotey.ai.site.creation.domain.repository.NovelRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.ChapterMapper;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.NovelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 小说查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NovelQueryService {

    private final NovelRepository novelRepository;
    private final NovelCategoryRepository novelCategoryRepository;
    private final ChapterRepository chapterRepository;
    private final NovelMapper novelMapper;
    private final ChapterMapper chapterMapper;

    /**
     * 分页查询小说
     */
    public PageResult<Novel> findPage(NovelQuery query) {
        List<Novel> novels = novelRepository.findPage(query);
        long total = novelRepository.count(query);

        // 加载分类
        for (Novel novel : novels) {
            loadCategory(novel);
        }

        return PageResult.of(novels, total);
    }

    /**
     * 根据ID查询小说
     */
    public Novel findById(Long id) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("小说不存在"));
        loadCategory(novel);
        return novel;
    }

    /**
     * 根据Slug查询小说
     */
    public Novel findBySlug(String slug) {
        Novel novel = novelRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("小说不存在"));
        loadCategory(novel);
        return novel;
    }

    /**
     * 查询已发布的小说
     */
    public PageResult<Novel> findPublished(NovelQuery query) {
        query.setStatus(NovelStatus.PUBLISHED.getCode());
        return findPage(query);
    }

    /**
     * 获取小说章节列表
     */
    public List<Chapter> getChapters(Long novelId) {
        return chapterRepository.findPublishedByNovelId(novelId);
    }

    /**
     * 获取小说所有章节（管理端）
     */
    public List<Chapter> getAllChapters(Long novelId) {
        return chapterRepository.findByNovelId(novelId);
    }

    /**
     * 加载分类
     */
    private void loadCategory(Novel novel) {
        if (novel.getCategoryId() != null) {
            novelCategoryRepository.findById(novel.getCategoryId())
                    .ifPresent(novel::setCategory);
        }
    }
}
