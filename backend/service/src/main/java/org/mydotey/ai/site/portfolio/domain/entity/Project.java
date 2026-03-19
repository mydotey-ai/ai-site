package org.mydotey.ai.site.portfolio.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;
import org.mydotey.ai.site.portfolio.domain.enums.ProjectStatus;

import java.util.List;

/**
 * 项目实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "project", autoResultMap = true)
public class Project extends BaseEntity {

    /**
     * 项目ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * URL别名
     */
    private String slug;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目详情(Markdown)
     */
    private String content;

    /**
     * 封面图URL
     */
    private String coverImage;

    /**
     * 技术栈列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> techStack;

    /**
     * 状态: DEVELOPING / RELEASED / ARCHIVED
     */
    private String status;

    /**
     * 标签列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<ProjectTag> tags;

    /**
     * 链接列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<ProjectLink> links;

    /**
     * 获取项目状态枚举
     */
    public ProjectStatus getStatusEnum() {
        return ProjectStatus.fromCode(this.status);
    }

    /**
     * 是否已发布
     */
    public boolean isReleased() {
        return ProjectStatus.RELEASED.getCode().equals(this.status);
    }
}
