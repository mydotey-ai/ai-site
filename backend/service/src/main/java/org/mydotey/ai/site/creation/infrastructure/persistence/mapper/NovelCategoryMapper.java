package org.mydotey.ai.site.creation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.creation.domain.entity.NovelCategory;

import java.util.List;

/**
 * 小说分类Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface NovelCategoryMapper extends BaseMapper<NovelCategory> {

    /**
     * 根据Slug查找分类
     */
    @Select("SELECT * FROM novel_category WHERE slug = #{slug} AND deleted = 0")
    NovelCategory findBySlug(@Param("slug") String slug);

    /**
     * 查找所有分类（按排序）
     */
    @Select("SELECT * FROM novel_category WHERE deleted = 0 ORDER BY sort ASC")
    List<NovelCategory> findAllOrderBySort();
}
