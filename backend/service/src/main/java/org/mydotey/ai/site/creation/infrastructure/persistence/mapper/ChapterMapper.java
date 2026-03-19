package org.mydotey.ai.site.creation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.creation.domain.entity.Chapter;

import java.util.List;

/**
 * 章节Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {

    /**
     * 查找小说的所有章节
     */
    @Select("SELECT * FROM chapter WHERE novel_id = #{novelId} AND deleted = 0 ORDER BY chapter_no ASC")
    List<Chapter> findByNovelId(@Param("novelId") Long novelId);

    /**
     * 查找小说的已发布章节
     */
    @Select("SELECT * FROM chapter WHERE novel_id = #{novelId} AND status = 'PUBLISHED' AND deleted = 0 ORDER BY chapter_no ASC")
    List<Chapter> findPublishedByNovelId(@Param("novelId") Long novelId);

    /**
     * 获取上一章
     */
    @Select("SELECT * FROM chapter WHERE novel_id = #{novelId} AND chapter_no < #{chapterNo} AND status = 'PUBLISHED' AND deleted = 0 ORDER BY chapter_no DESC LIMIT 1")
    Chapter findPrevChapter(@Param("novelId") Long novelId, @Param("chapterNo") Integer chapterNo);

    /**
     * 获取下一章
     */
    @Select("SELECT * FROM chapter WHERE novel_id = #{novelId} AND chapter_no > #{chapterNo} AND status = 'PUBLISHED' AND deleted = 0 ORDER BY chapter_no ASC LIMIT 1")
    Chapter findNextChapter(@Param("novelId") Long novelId, @Param("chapterNo") Integer chapterNo);

    /**
     * 获取最大章节序号
     */
    @Select("SELECT COALESCE(MAX(chapter_no), 0) FROM chapter WHERE novel_id = #{novelId} AND deleted = 0")
    int findMaxChapterNo(@Param("novelId") Long novelId);

    /**
     * 检查章节序号是否存在
     */
    @Select("SELECT COUNT(*) FROM chapter WHERE novel_id = #{novelId} AND chapter_no = #{chapterNo} AND deleted = 0")
    int countByNovelIdAndChapterNo(@Param("novelId") Long novelId, @Param("chapterNo") Integer chapterNo);

    /**
     * 检查章节序号是否存在（排除指定ID）
     */
    @Select("SELECT COUNT(*) FROM chapter WHERE novel_id = #{novelId} AND chapter_no = #{chapterNo} AND id != #{excludeId} AND deleted = 0")
    int countByNovelIdAndChapterNoExcludeId(@Param("novelId") Long novelId, @Param("chapterNo") Integer chapterNo, @Param("excludeId") Long excludeId);

    /**
     * 删除小说的所有章节
     */
    @Update("UPDATE chapter SET deleted = 1 WHERE novel_id = #{novelId}")
    void deleteByNovelId(@Param("novelId") Long novelId);

    /**
     * 统计小说的章节数
     */
    @Select("SELECT COUNT(*) FROM chapter WHERE novel_id = #{novelId} AND deleted = 0")
    long countByNovelId(@Param("novelId") Long novelId);

    /**
     * 统计小说的已发布章节数
     */
    @Select("SELECT COUNT(*) FROM chapter WHERE novel_id = #{novelId} AND status = 'PUBLISHED' AND deleted = 0")
    long countPublishedByNovelId(@Param("novelId") Long novelId);

    /**
     * 增加浏览量
     */
    @Update("UPDATE chapter SET view_count = view_count + 1 WHERE id = #{chapterId}")
    void incrementViewCount(@Param("chapterId") Long chapterId);
}
