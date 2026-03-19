package org.mydotey.ai.site.creation.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;
import org.mydotey.ai.site.creation.domain.enums.NovelStatus;

import java.util.List;

/**
 * 小说实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("novel")
public class Novel extends BaseEntity {

    /**
     * 小说ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 小说标题
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
     * 简介
     */
    private String summary;

    /**
     * 封面图URL
     */
    private String coverImage;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 状态: DRAFT / PUBLISHED / COMPLETED
     */
    private String status;

    /**
     * 总字数
     */
    private Integer wordCount;

    /**
     * 章节数
     */
    private Integer chapterCount;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 分类（非数据库字段）
     */
    @TableField(exist = false)
    private NovelCategory category;

    /**
     * 章节列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<Chapter> chapters;

    /**
     * 获取小说状态枚举
     */
    public NovelStatus getStatusEnum() {
        return NovelStatus.fromCode(this.status);
    }

    /**
     * 是否已发布
     */
    public boolean isPublished() {
        return NovelStatus.PUBLISHED.getCode().equals(this.status) ||
               NovelStatus.COMPLETED.getCode().equals(this.status);
    }
}
