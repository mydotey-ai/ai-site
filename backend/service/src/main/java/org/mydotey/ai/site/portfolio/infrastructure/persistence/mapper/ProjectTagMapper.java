package org.mydotey.ai.site.portfolio.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectTag;

import java.util.List;

/**
 * 项目标签Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface ProjectTagMapper extends BaseMapper<ProjectTag> {

    /**
     * 根据Slug查找标签
     */
    @Select("SELECT * FROM project_tag WHERE slug = #{slug} AND deleted = 0")
    ProjectTag findBySlug(@Param("slug") String slug);

    /**
     * 统计Slug数量
     */
    @Select("SELECT COUNT(*) FROM project_tag WHERE slug = #{slug} AND deleted = 0")
    int countBySlug(@Param("slug") String slug);

    /**
     * 统计Slug数量（排除指定ID）
     */
    @Select("SELECT COUNT(*) FROM project_tag WHERE slug = #{slug} AND id != #{excludeId} AND deleted = 0")
    int countBySlugExcludeId(@Param("slug") String slug, @Param("excludeId") Long excludeId);

    /**
     * 查找所有标签（按排序）
     */
    @Select("SELECT * FROM project_tag WHERE deleted = 0 ORDER BY sort ASC")
    List<ProjectTag> findAllOrderBySort();

    /**
     * 根据项目ID查找标签
     */
    @Select("SELECT pt.* FROM project_tag pt " +
            "INNER JOIN project_tag_relation ptr ON pt.id = ptr.tag_id " +
            "WHERE ptr.project_id = #{projectId} AND pt.deleted = 0 " +
            "ORDER BY pt.sort ASC")
    List<ProjectTag> findByProjectId(@Param("projectId") Long projectId);

    /**
     * 统计标签下的项目数量
     */
    @Select("SELECT COUNT(*) FROM project_tag_relation WHERE tag_id = #{tagId}")
    int countProjectsByTagId(@Param("tagId") Long tagId);
}
