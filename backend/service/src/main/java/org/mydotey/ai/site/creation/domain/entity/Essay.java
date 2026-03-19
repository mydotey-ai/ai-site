package org.mydotey.ai.site.creation.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;
import org.mydotey.ai.site.creation.domain.enums.CreationStatus;

/**
 * 散文实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("essay")
public class Essay extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String title;

    private String slug;

    private String author;

    private String summary;

    private String content;

    private Long categoryId;

    private String status;

    private Integer viewCount;

    @TableField(exist = false)
    private EssayCategory category;

    public CreationStatus getStatusEnum() {
        return CreationStatus.fromCode(this.status);
    }

    public boolean isPublished() {
        return CreationStatus.PUBLISHED.getCode().equals(this.status);
    }
}
