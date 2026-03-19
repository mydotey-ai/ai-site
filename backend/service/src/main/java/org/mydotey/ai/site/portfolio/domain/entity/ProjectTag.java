package org.mydotey.ai.site.portfolio.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;

/**
 * 项目标签实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("project_tag")
public class ProjectTag extends BaseEntity {

    /**
     * 标签ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * URL别名
     */
    private String slug;

    /**
     * 标签颜色(HEX)
     */
    private String color;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 项目数量（非数据库字段）
     */
    @TableField(exist = false)
    private Integer projectCount;
}
