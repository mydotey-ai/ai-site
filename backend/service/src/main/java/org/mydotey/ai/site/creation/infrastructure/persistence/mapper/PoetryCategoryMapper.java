package org.mydotey.ai.site.creation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.creation.domain.entity.PoetryCategory;

import java.util.List;

/**
 * 诗歌分类Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface PoetryCategoryMapper extends BaseMapper<PoetryCategory> {

    @Select("SELECT * FROM poetry_category WHERE slug = #{slug} AND deleted = 0")
    PoetryCategory findBySlug(@Param("slug") String slug);

    @Select("SELECT * FROM poetry_category WHERE deleted = 0 ORDER BY sort ASC")
    List<PoetryCategory> findAllOrderBySort();
}
