package org.mydotey.ai.site.creation.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.creation.domain.entity.Chapter;
import org.mydotey.ai.site.creation.domain.repository.ChapterRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.ChapterMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 章节仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class ChapterRepositoryImpl implements ChapterRepository {

    private final ChapterMapper chapterMapper;

    @Override
    public Optional<Chapter> findById(Long id) {
        return Optional.ofNullable(chapterMapper.selectById(id));
    }

    @Override
    public List<Chapter> findByNovelId(Long novelId) {
        return chapterMapper.findByNovelId(novelId);
    }

    @Override
    public List<Chapter> findPublishedByNovelId(Long novelId) {
        return chapterMapper.findPublishedByNovelId(novelId);
    }

    @Override
    public Optional<Chapter> findPrevChapter(Long novelId, Integer chapterNo) {
        return Optional.ofNullable(chapterMapper.findPrevChapter(novelId, chapterNo));
    }

    @Override
    public Optional<Chapter> findNextChapter(Long novelId, Integer chapterNo) {
        return Optional.ofNullable(chapterMapper.findNextChapter(novelId, chapterNo));
    }

    @Override
    public int findMaxChapterNo(Long novelId) {
        return chapterMapper.findMaxChapterNo(novelId);
    }

    @Override
    public boolean existsByNovelIdAndChapterNo(Long novelId, Integer chapterNo) {
        return chapterMapper.countByNovelIdAndChapterNo(novelId, chapterNo) > 0;
    }

    @Override
    public boolean existsByNovelIdAndChapterNoExcludeId(Long novelId, Integer chapterNo, Long excludeId) {
        return chapterMapper.countByNovelIdAndChapterNoExcludeId(novelId, chapterNo, excludeId) > 0;
    }

    @Override
    public void save(Chapter chapter) {
        chapterMapper.insert(chapter);
    }

    @Override
    public void update(Chapter chapter) {
        chapterMapper.updateById(chapter);
    }

    @Override
    public void deleteById(Long id) {
        chapterMapper.deleteById(id);
    }

    @Override
    public void deleteByNovelId(Long novelId) {
        chapterMapper.deleteByNovelId(novelId);
    }

    @Override
    public long countByNovelId(Long novelId) {
        return chapterMapper.countByNovelId(novelId);
    }

    @Override
    public long countPublishedByNovelId(Long novelId) {
        return chapterMapper.countPublishedByNovelId(novelId);
    }
}
