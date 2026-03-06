package org.mydotey.ai.site.auth.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;

/**
 * 角色实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("role")
public class Role extends BaseEntity {

    /**
     * 角色ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色代码
     */
    private String code;

    /**
     * 角色描述
     */
    private String description;
}