package org.mydotey.ai.site.creation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.creation.domain.entity.Novel;

/**
 * 小说Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface NovelMapper extends BaseMapper<Novel> {

    /**
     * 根据Slug查找小说
     */
    @Select("SELECT * FROM novel WHERE slug = #{slug} AND deleted = 0")
    Novel findBySlug(@Param("slug") String slug);

    /**
     * 统计Slug数量
     */
    @Select("SELECT COUNT(*) FROM novel WHERE slug = #{slug} AND deleted = 0")
    int countBySlug(@Param("slug") String slug);

    /**
     * 统计Slug数量（排除指定ID）
     */
    @Select("SELECT COUNT(*) FROM novel WHERE slug = #{slug} AND id != #{excludeId} AND deleted = 0")
    int countBySlugExcludeId(@Param("slug") String slug, @Param("excludeId") Long excludeId);

    /**
     * 更新小说统计信息
     */
    @Update("UPDATE novel SET word_count = (SELECT COALESCE(SUM(word_count), 0) FROM chapter WHERE novel_id = #{novelId} AND deleted = 0 AND status = 'PUBLISHED'), " +
            "chapter_count = (SELECT COUNT(*) FROM chapter WHERE novel_id = #{novelId} AND deleted = 0 AND status = 'PUBLISHED') " +
            "WHERE id = #{novelId}")
    void updateStats(@Param("novelId") Long novelId);

    /**
     * 增加浏览量
     */
    @Update("UPDATE novel SET view_count = view_count + 1 WHERE id = #{novelId}")
    void incrementViewCount(@Param("novelId") Long novelId);
}
