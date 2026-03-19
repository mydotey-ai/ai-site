package org.mydotey.ai.site.media.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;

/**
 * 文件夹实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("folder")
public class Folder extends BaseEntity {

    /**
     * 文件夹ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文件夹名称
     */
    private String name;

    /**
     * 父文件夹ID
     */
    private Long parentId;

    /**
     * 类型: IMAGE / VIDEO / AUDIO
     */
    private String type;

    /**
     * 排序
     */
    private Integer sort;
}
