package org.mydotey.ai.site.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;

/**
 * 分类实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("category")
public class Category extends BaseEntity {

    /**
     * 分类ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * URL别名
     */
    private String slug;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 文章数量
     */
    private Integer articleCount;
}
