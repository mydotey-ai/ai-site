package org.mydotey.ai.site.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;

/**
 * 标签实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tag")
public class Tag extends BaseEntity {

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
     * 标签颜色
     */
    private String color;

    /**
     * 文章数量
     */
    private Integer articleCount;
}
