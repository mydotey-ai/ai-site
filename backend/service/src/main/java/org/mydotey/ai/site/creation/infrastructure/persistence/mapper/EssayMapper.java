package org.mydotey.ai.site.creation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.creation.domain.entity.Essay;

/**
 * 散文Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface EssayMapper extends BaseMapper<Essay> {

    @Select("SELECT * FROM essay WHERE slug = #{slug} AND deleted = 0")
    Essay findBySlug(@Param("slug") String slug);

    @Select("SELECT COUNT(*) FROM essay WHERE slug = #{slug} AND deleted = 0")
    int countBySlug(@Param("slug") String slug);

    @Select("SELECT COUNT(*) FROM essay WHERE slug = #{slug} AND id != #{excludeId} AND deleted = 0")
    int countBySlugExcludeId(@Param("slug") String slug, @Param("excludeId") Long excludeId);

    @Update("UPDATE essay SET view_count = view_count + 1 WHERE id = #{essayId}")
    void incrementViewCount(@Param("essayId") Long essayId);
}
