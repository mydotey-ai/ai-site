package org.mydotey.ai.site.creation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.creation.domain.entity.EssayCategory;

import java.util.List;

/**
 * 散文分类Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface EssayCategoryMapper extends BaseMapper<EssayCategory> {

    @Select("SELECT * FROM essay_category WHERE slug = #{slug} AND deleted = 0")
    EssayCategory findBySlug(@Param("slug") String slug);

    @Select("SELECT * FROM essay_category WHERE deleted = 0 ORDER BY sort ASC")
    List<EssayCategory> findAllOrderBySort();
}
