package org.mydotey.ai.site.portfolio.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectLink;

import java.util.List;

/**
 * 项目链接Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface ProjectLinkMapper extends BaseMapper<ProjectLink> {

    /**
     * 根据项目ID查找链接
     */
    @Select("SELECT * FROM project_link WHERE project_id = #{projectId} ORDER BY sort ASC")
    List<ProjectLink> findByProjectId(@Param("projectId") Long projectId);

    /**
     * 删除项目的所有链接
     */
    @Delete("DELETE FROM project_link WHERE project_id = #{projectId}")
    void deleteByProjectId(@Param("projectId") Long projectId);

    /**
     * 批量插入链接
     */
    @Insert("<script>" +
            "INSERT INTO project_link (id, project_id, type, label, url, sort, created_at) VALUES " +
            "<foreach collection='links' item='link' separator=','>" +
            "(#{link.id}, #{link.projectId}, #{link.type}, #{link.label}, #{link.url}, #{link.sort}, #{link.createdAt})" +
            "</foreach>" +
            "</script>")
    void insertAll(@Param("links") List<ProjectLink> links);
}
