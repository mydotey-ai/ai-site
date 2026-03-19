package org.mydotey.ai.site.creation.domain.repository;

import org.mydotey.ai.site.creation.domain.entity.Chapter;

import java.util.List;
import java.util.Optional;

/**
 * 章节仓储接口
 *
 * @author AI-Site
 */
public interface ChapterRepository {

    /**
     * 根据ID查找章节
     */
    Optional<Chapter> findById(Long id);

    /**
     * 查找小说的所有章节
     */
    List<Chapter> findByNovelId(Long novelId);

    /**
     * 查找小说的已发布章节
     */
    List<Chapter> findPublishedByNovelId(Long novelId);

    /**
     * 获取上一章
     */
    Optional<Chapter> findPrevChapter(Long novelId, Integer chapterNo);

    /**
     * 获取下一章
     */
    Optional<Chapter> findNextChapter(Long novelId, Integer chapterNo);

    /**
     * 获取最大章节序号
     */
    int findMaxChapterNo(Long novelId);

    /**
     * 检查章节序号是否存在
     */
    boolean existsByNovelIdAndChapterNo(Long novelId, Integer chapterNo);

    /**
     * 检查章节序号是否存在（排除指定ID）
     */
    boolean existsByNovelIdAndChapterNoExcludeId(Long novelId, Integer chapterNo, Long excludeId);

    /**
     * 保存章节
     */
    void save(Chapter chapter);

    /**
     * 更新章节
     */
    void update(Chapter chapter);

    /**
     * 删除章节
     */
    void deleteById(Long id);

    /**
     * 删除小说的所有章节
     */
    void deleteByNovelId(Long novelId);

    /**
     * 统计小说的章节数
     */
    long countByNovelId(Long novelId);

    /**
     * 统计小说的已发布章节数
     */
    long countPublishedByNovelId(Long novelId);
}
