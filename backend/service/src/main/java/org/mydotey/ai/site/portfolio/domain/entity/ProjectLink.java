package org.mydotey.ai.site.portfolio.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;
import org.mydotey.ai.site.portfolio.domain.enums.ProjectLinkType;

/**
 * 项目链接实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("project_link")
public class ProjectLink extends BaseEntity {

    /**
     * 链接ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 链接类型: DEMO / SOURCE / DOCS / OTHER
     */
    private String type;

    /**
     * 链接标签
     */
    private String label;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 获取链接类型枚举
     */
    public ProjectLinkType getTypeEnum() {
        return ProjectLinkType.fromCode(this.type);
    }
}
