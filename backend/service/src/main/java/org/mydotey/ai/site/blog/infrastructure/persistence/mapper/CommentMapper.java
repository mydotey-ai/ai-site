package org.mydotey.ai.site.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.blog.domain.entity.Comment;

import java.util.List;

/**
 * 评论 Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 根据文章ID查找已通过的评论（树形结构）
     */
    @Select("SELECT * FROM comment WHERE article_id = #{articleId} AND status = 'APPROVED' AND deleted = 0 ORDER BY created_at ASC")
    List<Comment> findApprovedByArticleId(@Param("articleId") Long articleId);

    /**
     * 根据状态查找评论
     */
    @Select("SELECT * FROM comment WHERE status = #{status} AND deleted = 0 ORDER BY created_at DESC")
    IPage<Comment> findByStatus(Page<Comment> page, @Param("status") String status);

    /**
     * 统计文章的评论数
     */
    @Select("SELECT COUNT(*) FROM comment WHERE article_id = #{articleId} AND status = 'APPROVED' AND deleted = 0")
    long countByArticleId(@Param("articleId") Long articleId);

    /**
     * 统计待审核评论数
     */
    @Select("SELECT COUNT(*) FROM comment WHERE status = 'PENDING' AND deleted = 0")
    long countPending();

    /**
     * 更新状态
     */
    @Update("UPDATE comment SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 批量更新状态
     */
    @Update("<script>" +
            "UPDATE comment SET status = #{status} WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);

    /**
     * 增加点赞数
     */
    @Update("UPDATE comment SET like_count = like_count + 1 WHERE id = #{id}")
    void incrementLikeCount(@Param("id") Long id);
}
