package org.mydotey.ai.site.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.blog.domain.entity.Tag;

import java.util.List;

/**
 * 标签 Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据Slug查找标签
     */
    @Select("SELECT * FROM tag WHERE slug = #{slug} AND deleted = 0")
    Tag findBySlug(@Param("slug") String slug);

    /**
     * 检查Slug是否存在
     */
    @Select("SELECT COUNT(*) FROM tag WHERE slug = #{slug} AND deleted = 0")
    int countBySlug(@Param("slug") String slug);

    /**
     * 检查Slug是否存在（排除指定ID）
     */
    @Select("SELECT COUNT(*) FROM tag WHERE slug = #{slug} AND id != #{excludeId} AND deleted = 0")
    int countBySlugExcludeId(@Param("slug") String slug, @Param("excludeId") Long excludeId);

    /**
     * 查找所有标签
     */
    @Select("SELECT * FROM tag WHERE deleted = 0 ORDER BY name ASC")
    List<Tag> findAllOrderByName();

    /**
     * 根据文章ID查找标签
     */
    @Select("SELECT t.* FROM tag t " +
            "INNER JOIN article_tag at ON t.id = at.tag_id " +
            "WHERE at.article_id = #{articleId} AND t.deleted = 0")
    List<Tag> findByArticleId(@Param("articleId") Long articleId);

    /**
     * 更新文章数量
     */
    @Update("UPDATE tag SET article_count = #{count} WHERE id = #{id}")
    void updateArticleCount(@Param("id") Long id, @Param("count") int count);
}
