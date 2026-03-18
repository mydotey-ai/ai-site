package org.mydotey.ai.site.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.blog.domain.entity.Article;

import java.util.List;

/**
 * 文章 Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据Slug查找文章
     */
    @Select("SELECT * FROM article WHERE slug = #{slug} AND deleted = 0")
    Article findBySlug(@Param("slug") String slug);

    /**
     * 检查Slug是否存在
     */
    @Select("SELECT COUNT(*) FROM article WHERE slug = #{slug} AND deleted = 0")
    int countBySlug(@Param("slug") String slug);

    /**
     * 检查Slug是否存在（排除指定ID）
     */
    @Select("SELECT COUNT(*) FROM article WHERE slug = #{slug} AND id != #{excludeId} AND deleted = 0")
    int countBySlugExcludeId(@Param("slug") String slug, @Param("excludeId") Long excludeId);

    /**
     * 增加浏览次数
     */
    @Update("UPDATE article SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(@Param("id") Long id);

    /**
     * 增加点赞数
     */
    @Update("UPDATE article SET like_count = like_count + 1 WHERE id = #{id}")
    void incrementLikeCount(@Param("id") Long id);

    /**
     * 查找文章的标签ID列表
     */
    @Select("SELECT tag_id FROM article_tag WHERE article_id = #{articleId}")
    List<Long> findTagIdsByArticleId(@Param("articleId") Long articleId);

    /**
     * 删除文章标签关联
     */
    @Delete("DELETE FROM article_tag WHERE article_id = #{articleId}")
    void deleteArticleTags(@Param("articleId") Long articleId);

    /**
     * 批量插入文章标签关联
     */
    @Insert("<script>" +
            "INSERT INTO article_tag (id, article_id, tag_id, created_at) VALUES " +
            "<foreach collection='tagIds' item='tagId' separator=','>" +
            "(#{articleId}, #{articleId}, #{tagId}, NOW())" +
            "</foreach>" +
            "</script>")
    void insertArticleTags(@Param("articleId") Long articleId, @Param("tagIds") List<Long> tagIds);

    /**
     * 根据标签ID分页查询文章
     */
    @Select("SELECT a.* FROM article a " +
            "INNER JOIN article_tag at ON a.id = at.article_id " +
            "WHERE at.tag_id = #{tagId} AND a.status = 1 AND a.deleted = 0 " +
            "ORDER BY a.is_top DESC, a.published_at DESC")
    IPage<Article> findByTagId(Page<Article> page, @Param("tagId") Long tagId);

    /**
     * 搜索文章
     */
    @Select("SELECT * FROM article " +
            "WHERE status = 1 AND deleted = 0 " +
            "AND (title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR summary LIKE CONCAT('%', #{keyword}, '%') " +
            "OR content LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY is_top DESC, published_at DESC")
    IPage<Article> search(Page<Article> page, @Param("keyword") String keyword);

    /**
     * 查找已发布文章（按置顶和发布时间排序）
     */
    @Select("SELECT * FROM article WHERE status = 1 AND deleted = 0 ORDER BY is_top DESC, published_at DESC")
    IPage<Article> findPublished(Page<Article> page);

    /**
     * 根据分类ID分页查询文章
     */
    @Select("SELECT * FROM article WHERE category_id = #{categoryId} AND status = 1 AND deleted = 0 ORDER BY is_top DESC, published_at DESC")
    IPage<Article> findByCategoryId(Page<Article> page, @Param("categoryId") Long categoryId);
}
