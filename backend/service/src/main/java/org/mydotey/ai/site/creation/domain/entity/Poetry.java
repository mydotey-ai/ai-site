package org.mydotey.ai.site.creation.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;
import org.mydotey.ai.site.creation.domain.enums.CreationStatus;

/**
 * 诗歌实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("poetry")
public class Poetry extends BaseEntity {

    /**
     * 诗歌ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * URL别名
     */
    private String slug;

    /**
     * 作者
     */
    private String author;

    /**
     * 内容
     */
    private String content;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 状态: DRAFT / PUBLISHED
     */
    private String status;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 分类（非数据库字段）
     */
    @TableField(exist = false)
    private PoetryCategory category;

    /**
     * 获取状态枚举
     */
    public CreationStatus getStatusEnum() {
        return CreationStatus.fromCode(this.status);
    }

    /**
     * 是否已发布
     */
    public boolean isPublished() {
        return CreationStatus.PUBLISHED.getCode().equals(this.status);
    }
}
