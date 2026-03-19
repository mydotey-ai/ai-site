package org.mydotey.ai.site.portfolio.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.portfolio.domain.entity.Project;

import java.util.List;

/**
 * 项目Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 根据Slug查找项目
     */
    @Select("SELECT * FROM project WHERE slug = #{slug} AND deleted = 0")
    Project findBySlug(@Param("slug") String slug);

    /**
     * 统计Slug数量
     */
    @Select("SELECT COUNT(*) FROM project WHERE slug = #{slug} AND deleted = 0")
    int countBySlug(@Param("slug") String slug);

    /**
     * 统计Slug数量（排除指定ID）
     */
    @Select("SELECT COUNT(*) FROM project WHERE slug = #{slug} AND id != #{excludeId} AND deleted = 0")
    int countBySlugExcludeId(@Param("slug") String slug, @Param("excludeId") Long excludeId);

    /**
     * 根据标签ID查找项目
     */
    @Select("SELECT p.* FROM project p " +
            "INNER JOIN project_tag_relation ptr ON p.id = ptr.project_id " +
            "WHERE ptr.tag_id = #{tagId} AND p.deleted = 0 " +
            "ORDER BY p.created_at DESC")
    Page<Project> findByTagId(Page<Project> page, @Param("tagId") Long tagId);

    /**
     * 查找项目的标签ID列表
     */
    @Select("SELECT tag_id FROM project_tag_relation WHERE project_id = #{projectId}")
    List<Long> findTagIdsByProjectId(@Param("projectId") Long projectId);

    /**
     * 插入项目标签关联
     */
    @Insert("<script>" +
            "INSERT INTO project_tag_relation (project_id, tag_id) VALUES " +
            "<foreach collection='tagIds' item='tagId' separator=','>" +
            "(#{projectId}, #{tagId})" +
            "</foreach>" +
            "</script>")
    void insertProjectTags(@Param("projectId") Long projectId, @Param("tagIds") List<Long> tagIds);

    /**
     * 删除项目标签关联
     */
    @Delete("DELETE FROM project_tag_relation WHERE project_id = #{projectId}")
    void deleteProjectTags(@Param("projectId") Long projectId);
}
