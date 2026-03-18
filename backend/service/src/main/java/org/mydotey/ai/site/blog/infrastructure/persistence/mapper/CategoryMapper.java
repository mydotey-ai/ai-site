package org.mydotey.ai.site.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.blog.domain.entity.Category;

import java.util.List;

/**
 * 分类 Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 根据Slug查找分类
     */
    @Select("SELECT * FROM category WHERE slug = #{slug} AND deleted = 0")
    Category findBySlug(@Param("slug") String slug);

    /**
     * 检查Slug是否存在
     */
    @Select("SELECT COUNT(*) FROM category WHERE slug = #{slug} AND deleted = 0")
    int countBySlug(@Param("slug") String slug);

    /**
     * 检查Slug是否存在（排除指定ID）
     */
    @Select("SELECT COUNT(*) FROM category WHERE slug = #{slug} AND id != #{excludeId} AND deleted = 0")
    int countBySlugExcludeId(@Param("slug") String slug, @Param("excludeId") Long excludeId);

    /**
     * 查找所有分类（按排序）
     */
    @Select("SELECT * FROM category WHERE deleted = 0 ORDER BY sort_order ASC")
    List<Category> findAllOrderBySortOrder();

    /**
     * 查找子分类
     */
    @Select("SELECT * FROM category WHERE parent_id = #{parentId} AND deleted = 0 ORDER BY sort_order ASC")
    List<Category> findByParentId(@Param("parentId") Long parentId);

    /**
     * 更新文章数量
     */
    @Update("UPDATE category SET article_count = #{count} WHERE id = #{id}")
    void updateArticleCount(@Param("id") Long id, @Param("count") int count);
}
