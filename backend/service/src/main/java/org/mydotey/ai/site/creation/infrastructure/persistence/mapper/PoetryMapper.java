package org.mydotey.ai.site.creation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.creation.domain.entity.Poetry;

/**
 * 诗歌Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface PoetryMapper extends BaseMapper<Poetry> {

    @Select("SELECT * FROM poetry WHERE slug = #{slug} AND deleted = 0")
    Poetry findBySlug(@Param("slug") String slug);

    @Select("SELECT COUNT(*) FROM poetry WHERE slug = #{slug} AND deleted = 0")
    int countBySlug(@Param("slug") String slug);

    @Select("SELECT COUNT(*) FROM poetry WHERE slug = #{slug} AND id != #{excludeId} AND deleted = 0")
    int countBySlugExcludeId(@Param("slug") String slug, @Param("excludeId") Long excludeId);

    @Update("UPDATE poetry SET view_count = view_count + 1 WHERE id = #{poetryId}")
    void incrementViewCount(@Param("poetryId") Long poetryId);
}
