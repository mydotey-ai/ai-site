package org.mydotey.ai.site.media.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;

/**
 * 相册实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("album")
public class Album extends BaseEntity {

    /**
     * 相册ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 相册名称
     */
    private String name;

    /**
     * URL别名
     */
    private String slug;

    /**
     * 描述
     */
    private String description;

    /**
     * 封面图URL
     */
    private String coverImage;

    /**
     * 图片数量
     */
    private Integer imageCount;

    /**
     * 是否公开
     */
    private Integer isPublic;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否公开
     */
    public boolean isPublic() {
        return Integer.valueOf(1).equals(this.isPublic);
    }
}
