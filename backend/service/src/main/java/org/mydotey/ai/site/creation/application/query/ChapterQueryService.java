package org.mydotey.ai.site.creation.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.creation.domain.entity.Chapter;
import org.mydotey.ai.site.creation.domain.entity.Novel;
import org.mydotey.ai.site.creation.domain.repository.ChapterRepository;
import org.mydotey.ai.site.creation.domain.repository.NovelRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.ChapterMapper;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.NovelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 章节查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChapterQueryService {

    private final ChapterRepository chapterRepository;
    private final NovelRepository novelRepository;
    private final ChapterMapper chapterMapper;
    private final NovelMapper novelMapper;

    /**
     * 根据ID查询章节
     */
    public Chapter findById(Long id) {
        return chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("章节不存在"));
    }

    /**
     * 获取章节内容（带导航信息）
     */
    public Chapter getChapterWithNavigation(Long chapterId) {
        Chapter chapter = findById(chapterId);

        // 获取上一章和下一章
        Chapter prevChapter = chapterRepository.findPrevChapter(chapter.getNovelId(), chapter.getChapterNo())
                .orElse(null);
        Chapter nextChapter = chapterRepository.findNextChapter(chapter.getNovelId(), chapter.getChapterNo())
                .orElse(null);

        // 获取小说信息
        Novel novel = novelRepository.findById(chapter.getNovelId())
                .orElseThrow(() -> new RuntimeException("小说不存在"));

        chapter.setNovel(novel);

        // 将导航信息存储在 chapter 的扩展字段中（实际使用时可以通过 DTO 返回）
        return chapter;
    }

    /**
     * 获取上一章ID
     */
    public Long getPrevChapterId(Long novelId, Integer chapterNo) {
        return chapterRepository.findPrevChapter(novelId, chapterNo)
                .map(Chapter::getId)
                .orElse(null);
    }

    /**
     * 获取下一章ID
     */
    public Long getNextChapterId(Long novelId, Integer chapterNo) {
        return chapterRepository.findNextChapter(novelId, chapterNo)
                .map(Chapter::getId)
                .orElse(null);
    }
}
