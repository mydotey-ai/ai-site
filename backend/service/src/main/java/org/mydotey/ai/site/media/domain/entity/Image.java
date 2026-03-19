package org.mydotey.ai.site.media.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;

import java.util.List;

/**
 * 图片实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "image", autoResultMap = true)
public class Image extends BaseEntity {

    /**
     * 图片ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 存储文件名
     */
    private String fileName;

    /**
     * 图片URL
     */
    private String url;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;

    /**
     * 文件大小（字节）
     */
    private Long size;

    /**
     * MIME类型
     */
    private String mimeType;

    /**
     * 相册ID
     */
    private Long albumId;

    /**
     * 文件夹ID
     */
    private Long folderId;

    /**
     * 标签列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags;

    /**
     * 是否公开
     */
    private Integer isPublic;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 相册（非数据库字段）
     */
    @TableField(exist = false)
    private Album album;

    /**
     * 是否公开
     */
    public boolean isPublic() {
        return Integer.valueOf(1).equals(this.isPublic);
    }
}
