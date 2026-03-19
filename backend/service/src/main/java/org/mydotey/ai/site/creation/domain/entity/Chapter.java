package org.mydotey.ai.site.creation.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;
import org.mydotey.ai.site.creation.domain.enums.ChapterStatus;

/**
 * 章节实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("chapter")
public class Chapter extends BaseEntity {

    /**
     * 章节ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 小说ID
     */
    private Long novelId;

    /**
     * 章节标题
     */
    private String title;

    /**
     * 章节内容
     */
    private String content;

    /**
     * 字数
     */
    private Integer wordCount;

    /**
     * 章节序号
     */
    private Integer chapterNo;

    /**
     * 状态: DRAFT / PUBLISHED
     */
    private String status;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 小说（非数据库字段）
     */
    @TableField(exist = false)
    private Novel novel;

    /**
     * 获取章节状态枚举
     */
    public ChapterStatus getStatusEnum() {
        return ChapterStatus.fromCode(this.status);
    }

    /**
     * 是否已发布
     */
    public boolean isPublished() {
        return ChapterStatus.PUBLISHED.getCode().equals(this.status);
    }
}
